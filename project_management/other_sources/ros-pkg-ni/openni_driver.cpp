/*
 * Software License Agreement (BSD License)
 *
 *  Copyright (c) 2011 2011 Willow Garage, Inc.
 *    Suat Gedikli <gedikli@willowgarage.com>
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of Willow Garage, Inc. nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 */
#include <openni_camera/openni_driver.h>
#include <openni_camera/openni_device_kinect.h>
#include <openni_camera/openni_device_primesense.h>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <locale>
#include <cctype>
#include <libusb-1.0/libusb.h>
#include <map>

using namespace std;
using namespace boost;

namespace openni_wrapper
{

OpenNIDriver::OpenNIDriver () throw (OpenNIException)
{
  // Initialize the Engine
  XnStatus status = context_.Init ();
  if (status != XN_STATUS_OK)
    THROW_OPENNI_EXCEPTION ("initialization failed. Reason: %s", xnGetStatusString (status));

  // clear current list of devices
  device_context_.clear ();
  // clear maps
  bus_map_.clear ();
  serial_map_.clear ();

  // enumerate all devices
  static xn::NodeInfoList node_info_list;
  status = context_.EnumerateProductionTrees (XN_NODE_TYPE_DEVICE, NULL, node_info_list);
  if (status != XN_STATUS_OK && node_info_list.Begin () != node_info_list.End ())
    THROW_OPENNI_EXCEPTION ("enumerating devices failed. Reason: %s", xnGetStatusString (status));
  else if (node_info_list.Begin () == node_info_list.End ())
    return; // no exception
  //THROW_OPENNI_EXCEPTION ("no compatible device found");

  vector<xn::NodeInfo> device_info;
  for (xn::NodeInfoList::Iterator nodeIt = node_info_list.Begin (); nodeIt != node_info_list.End (); ++nodeIt)
  {
    device_info.push_back (*nodeIt);
  }

  // enumerate depth nodes
  static xn::NodeInfoList depth_nodes;
  status = context_.EnumerateProductionTrees (XN_NODE_TYPE_DEPTH, NULL, depth_nodes, NULL);
  if (status != XN_STATUS_OK)
    THROW_OPENNI_EXCEPTION ("enumerating depth generators failed. Reason: %s", xnGetStatusString (status));

  vector<xn::NodeInfo> depth_info;
  for (xn::NodeInfoList::Iterator nodeIt = depth_nodes.Begin (); nodeIt != depth_nodes.End (); ++nodeIt)
  {
    depth_info.push_back (*nodeIt);
  }

  // enumerate image nodes
  static xn::NodeInfoList image_nodes;
  status = context_.EnumerateProductionTrees (XN_NODE_TYPE_IMAGE, NULL, image_nodes, NULL);
  if (status != XN_STATUS_OK)
    THROW_OPENNI_EXCEPTION ("enumerating image generators failed. Reason: %s", xnGetStatusString (status));

  vector<xn::NodeInfo> image_info;
  for (xn::NodeInfoList::Iterator nodeIt = image_nodes.Begin (); nodeIt != image_nodes.End (); ++nodeIt)
  {
    image_info.push_back (*nodeIt);
  }

  // check if we have same number of streams as devices!
  if (device_info.size () != depth_info.size () || device_info.size () != image_info.size ())
    THROW_OPENNI_EXCEPTION ("number of streams and devices does not match: %d devices, %d depth streams, %d image streams",
                            device_info.size (), depth_info.size (), image_info.size ());

  // add context object for each found device
  for (unsigned deviceIdx = 0; deviceIdx < device_info.size (); ++deviceIdx)
  {
    // add context object for device
    device_context_.push_back (DeviceContext (device_info[deviceIdx], image_info[deviceIdx], depth_info[deviceIdx]));

    // register bus@address to the corresponding context object
    unsigned short vendor_id;
    unsigned short product_id;
    unsigned char bus;
    unsigned char address;
    sscanf (device_info[deviceIdx].GetCreationInfo (), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);
    bus_map_ [bus][address] = deviceIdx;
  }

  // get additional info about connected devices like serial number, vendor name and prduct name
  getDeviceInfos ();

  // build serial number -> device index map
  for (unsigned deviceIdx = 0; deviceIdx < device_info.size (); ++deviceIdx)
  {
    string serial_number = getSerialNumber (deviceIdx);
    if (!serial_number.empty ())
      serial_map_[serial_number] = deviceIdx;
  }
}

void OpenNIDriver::stopAll () throw (OpenNIException)
{
  XnStatus status = context_.StopGeneratingAll ();
  if (status != XN_STATUS_OK)
    THROW_OPENNI_EXCEPTION ("stopping all streams failed. Reason: %s", xnGetStatusString (status));
}

OpenNIDriver::~OpenNIDriver () throw ()
{
  // no exception during destuctor
  try
  {
    stopAll ();
  }
  catch (...)
  {
  }

  context_.Shutdown ();
}

shared_ptr<OpenNIDevice> OpenNIDriver::getDeviceByIndex (unsigned index) const throw (OpenNIException)
{
  if (index > device_context_.size ())
    THROW_OPENNI_EXCEPTION ("device index out of range. only %d devices connected but device %d requested.", device_context_.size (), index);

  shared_ptr<OpenNIDevice> device = device_context_[index].device.lock ();
  if (!device)
  {
    string connection_string = device_context_[index].device_node.GetCreationInfo ();
    transform (connection_string.begin (), connection_string.end (), connection_string.begin (), std::towlower);
    if (connection_string.substr (0, 4) == "045e")
    {
      device = boost::shared_ptr<OpenNIDevice > (new DeviceKinect (context_, device_context_[index].device_node,
                                                                   device_context_[index].image_node, device_context_[index].depth_node));
      device_context_[index].device = device;
    }
    else if (connection_string.substr (0, 4) == "1d27")
    {
      device = boost::shared_ptr<OpenNIDevice > (new DevicePrimesense (context_, device_context_[index].device_node,
                                                                       device_context_[index].image_node, device_context_[index].depth_node));
      device_context_[index].device = device;
    }
    else
    {
      THROW_OPENNI_EXCEPTION ("vendor %s (%s) known by primesense driver, but not by ros driver. Contact maintainer of the ros driver.",
                              getVendorName (index), connection_string.substr (0, 4).c_str ());
    }
  }
  return device;
}

shared_ptr<OpenNIDevice> OpenNIDriver::getDeviceBySerialNumber (const string& serial_number) const throw (OpenNIException)
{
  map<string, unsigned>::const_iterator it = serial_map_.find (serial_number);

  if (it != serial_map_.end ())
  {
    return getDeviceByIndex (it->second);
  }

  THROW_OPENNI_EXCEPTION ("No device with serial number \'%s\' found", serial_number.c_str ());

  // because of warnings!!!
  return shared_ptr<OpenNIDevice > ((OpenNIDevice*)NULL);
}

shared_ptr<OpenNIDevice> OpenNIDriver::getDeviceByAddress (unsigned char bus, unsigned char address) const throw (OpenNIException)
{
  map<unsigned char, map<unsigned char, unsigned> >::const_iterator busIt = bus_map_.find (bus);
  if (busIt != bus_map_.end ())
  {
    map<unsigned char, unsigned>::const_iterator devIt = busIt->second.find (address);
    if (devIt != busIt->second.end ())
    {
      return getDeviceByIndex (devIt->second);
    }
  }

  THROW_OPENNI_EXCEPTION ("No device on bus: %d @ %d found", (int)bus, (int)address);

  // because of warnings!!!
  return shared_ptr<OpenNIDevice > ((OpenNIDevice*)NULL);
}

void OpenNIDriver::getDeviceInfos () throw ()
{
  libusb_context *context = NULL;
  int result;
  result = libusb_init (&context); //initialize a library session

  if (result < 0)
    return;

  libusb_device **devices;
  int count = libusb_get_device_list (context, &devices);
  if (count < 0)
    return;

  for (int devIdx = 0; devIdx < count; ++devIdx)
  {
    libusb_device* device = devices[devIdx];
    uint8_t busId = libusb_get_bus_number (device);
    map<unsigned char, map<unsigned char, unsigned> >::const_iterator busIt = bus_map_.find (busId);
    if (busIt == bus_map_.end ())
      continue;

    uint8_t address = libusb_get_device_address (device);
    map<unsigned char, unsigned>::const_iterator addressIt = busIt->second.find (address);
    if (addressIt == busIt->second.end ())
      continue;

    unsigned nodeIdx = addressIt->second;
    xn::NodeInfo& current_node = device_context_[nodeIdx].device_node;
    XnProductionNodeDescription& description = const_cast<XnProductionNodeDescription&>(current_node.GetDescription ());

    libusb_device_descriptor descriptor;
    result = libusb_get_device_descriptor (devices[devIdx], &descriptor);

    if (result < 0)
    {
      strcpy (description.strVendor, "unknown");
      strcpy (description.strName, "unknown");
      current_node.SetInstanceName ("");
    }
    else
    {
      libusb_device_handle* dev_handle;
      result = libusb_open (device, &dev_handle);
      if (result < 0)
      {
        strcpy (description.strVendor, "unknown");
        strcpy (description.strName, "unknown");
        current_node.SetInstanceName ("");
      }
      else
      {
        unsigned char buffer[1024];
        libusb_get_string_descriptor_ascii (dev_handle, descriptor.iManufacturer, buffer, 1024);
        strcpy (description.strVendor, (char*)buffer);

        libusb_get_string_descriptor_ascii (dev_handle, descriptor.iProduct, buffer, 1024);
        strcpy (description.strName, (char*)buffer);

        int len = libusb_get_string_descriptor_ascii (dev_handle, descriptor.iSerialNumber, buffer, 1024);
        if (len > 4)
          current_node.SetInstanceName ((char*)buffer);
        else
          current_node.SetInstanceName ("");

        libusb_close (dev_handle);
      }
    }
  }
  libusb_free_device_list (devices, 1);
  libusb_exit (context);
}

const char* OpenNIDriver::getSerialNumber (unsigned index) const throw ()
{
  return device_context_[index].device_node.GetInstanceName ();
}

const char* OpenNIDriver::getConnectionString (unsigned index) const throw ()
{
  return device_context_[index].device_node.GetCreationInfo ();
}

const char* OpenNIDriver::getVendorName (unsigned index) const throw ()
{
  return device_context_[index].device_node.GetDescription ().strVendor;
}

const char* OpenNIDriver::getProductName (unsigned index) const throw ()
{
  return device_context_[index].device_node.GetDescription ().strName;
}

unsigned short OpenNIDriver::getVendorID (unsigned index) const throw ()
{
  unsigned short vendor_id;
  unsigned short product_id;
  unsigned char bus;
  unsigned char address;
  sscanf (device_context_[index].device_node.GetCreationInfo (), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);

  return vendor_id;
}

unsigned short OpenNIDriver::getProductID (unsigned index) const throw ()
{
  unsigned short vendor_id;
  unsigned short product_id;
  unsigned char bus;
  unsigned char address;
  sscanf (device_context_[index].device_node.GetCreationInfo (), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);

  return product_id;
}

unsigned char OpenNIDriver::getBus (unsigned index) const throw ()
{
  unsigned short vendor_id;
  unsigned short product_id;
  unsigned char bus;
  unsigned char address;
  sscanf (device_context_[index].device_node.GetCreationInfo (), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);

  return bus;
}

unsigned char OpenNIDriver::getAddress (unsigned index) const throw ()
{
  unsigned short vendor_id;
  unsigned short product_id;
  unsigned char bus;
  unsigned char address;
  sscanf (device_context_[index].device_node.GetCreationInfo (), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);

  return address;
}

OpenNIDriver::DeviceContext::DeviceContext (const xn::NodeInfo& device, const xn::NodeInfo& image, const xn::NodeInfo& depth)
: device_node (device)
, image_node (image)
, depth_node (depth)
{
}

OpenNIDriver::DeviceContext::DeviceContext (const DeviceContext& other)
: device_node (other.device_node)
, image_node (other.image_node)
, depth_node (other.depth_node)
, device (other.device)
{
}

} // namespace
