/* Generated file, do not edit */



#ifndef CXXTEST_RUNNING
#define CXXTEST_RUNNING
#endif

#define _CXXTEST_HAVE_STD
#define _CXXTEST_HAVE_EH

#define CXXTEST_TRAP_SIGNALS
#define CXXTEST_TRACE_STACK
#define _CXXTEST_ABORT_TEST_ON_FAIL

#include <cxxtest/TestListener.h>
#include <cxxtest/TestTracker.h>
#include <cxxtest/TestRunner.h>
#include <cxxtest/RealDescriptions.h>
#include <cxxtest/ListenerList.h>
#include <cxxtest/XmlStdioPrinter.h>


typedef const CxxTest::SuiteDescription *SuiteDescriptionPtr;
typedef const CxxTest::TestDescription *TestDescriptionPtr;

// BEGIN: Test world (declarations of all test suites)

// Test suite: MyServiceTest

#include "MyServiceTest.h"
static MyServiceTest *suite_MyServiceTest;
static CxxTest::List Tests_MyServiceTest;

CxxTest::StaticSuiteDescription suiteDescription_MyServiceTest;


static class TestDescription_MyServiceTest_testAdd2 : public CxxTest::RealTestDescription {
public:
	void runTest() { if(suite_MyServiceTest) suite_MyServiceTest->testAdd2(); }
} testDescription_MyServiceTest_testAdd2;



// END: Test world

namespace CxxTest
{
	void initialize()
	{
		// Initialize test suite: MyServiceTest
		Tests_MyServiceTest.initialize();

		_TS_TRY_WITH_SIGNAL_PROTECTION {
			_TS_TRY { suite_MyServiceTest = new MyServiceTest; } _TS_PROPAGATE_SIGNAL _TS_CATCH_ABORT( {} )
			_TS_LAST_CATCH( { CxxTest::__cxxtest_failed_init_suites.addSuite("MyServiceTest", "Exception thrown when initializing " "MyServiceTest"); } )
		} _TS_CATCH_SIGNAL( { CxxTest::__cxxtest_failed_init_suites.addSuite("MyServiceTest", CxxTest::__cxxtest_sigmsg.c_str()); } );

		suiteDescription_MyServiceTest.initialize(
			"MyServiceTest.h", 15,
			"MyServiceTest", *suite_MyServiceTest, Tests_MyServiceTest);
		testDescription_MyServiceTest_testAdd2.initialize(Tests_MyServiceTest, suiteDescription_MyServiceTest, 29, "testAdd2");


	}
	
	void cleanup()
	{
		delete suite_MyServiceTest;

	}
}

#include <cxxtest/Root.cpp>


#include <dereferee-src/allocation_info_impl.cpp>
#include <dereferee-src/cxxtest_listener.cpp>
#include <dereferee-src/manager.cpp>
#include <dereferee-src/memtab.cpp>
#include <dereferee-src/usage_stats_impl.cpp>
#include <dereferee-src/gcc_macosx_platform.cpp>

class CxxTestMain
{
public:
	CxxTestMain()
	{
CxxTest::initialize();

		CxxTest::ListenerList listeners;
		CxxTest::XmlStdioPrinter listener_1; listeners.addListener(listener_1);

		CxxTest::TestRunner::runAllTests(listeners);
		CxxTest::__cxxtest_runCompleted = true;
		CxxTest::cleanup();
	}
};

CxxTestMain cxxTestMain;



