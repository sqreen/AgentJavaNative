/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_sqreen_powerwaf_Additive */

#ifndef _Included_io_sqreen_powerwaf_Additive
#define _Included_io_sqreen_powerwaf_Additive
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_sqreen_powerwaf_Additive
 * Method:    initAdditive
 * Signature: (Lio/sqreen/powerwaf/PowerwafHandle;)J
 */
JNIEXPORT jlong JNICALL Java_io_sqreen_powerwaf_Additive_initAdditive
  (JNIEnv *, jclass, jobject);

/*
 * Class:     io_sqreen_powerwaf_Additive
 * Method:    runAdditive
 * Signature: (Ljava/util/Map;Lio/sqreen/powerwaf/Powerwaf$Limits;Lio/sqreen/powerwaf/PowerwafMetrics;)Lio/sqreen/powerwaf/Powerwaf$ActionWithData;
 */
JNIEXPORT jobject JNICALL Java_io_sqreen_powerwaf_Additive_runAdditive__Ljava_util_Map_2Lio_sqreen_powerwaf_Powerwaf_00024Limits_2Lio_sqreen_powerwaf_PowerwafMetrics_2
  (JNIEnv *, jobject, jobject, jobject, jobject);

/*
 * Class:     io_sqreen_powerwaf_Additive
 * Method:    runAdditive
 * Signature: (Ljava/nio/ByteBuffer;Lio/sqreen/powerwaf/Powerwaf$Limits;Lio/sqreen/powerwaf/PowerwafMetrics;)Lio/sqreen/powerwaf/Powerwaf$ActionWithData;
 */
JNIEXPORT jobject JNICALL Java_io_sqreen_powerwaf_Additive_runAdditive__Ljava_nio_ByteBuffer_2Lio_sqreen_powerwaf_Powerwaf_00024Limits_2Lio_sqreen_powerwaf_PowerwafMetrics_2
  (JNIEnv *, jobject, jobject, jobject, jobject);

/*
 * Class:     io_sqreen_powerwaf_Additive
 * Method:    clearAdditive
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_io_sqreen_powerwaf_Additive_clearAdditive
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
