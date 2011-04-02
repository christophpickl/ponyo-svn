#include <jni.h>
#include "jniplay_JavaApp.h"

JNIEXPORT jint JNICALL Java_jniplay_JavaApp_nativeAdd(JNIEnv * env, jobject obj, jint x, jint y) {
	return x + y;
}
