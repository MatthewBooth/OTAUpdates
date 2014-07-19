LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 RootTools

LOCAL_PACKAGE_NAME := OTAUpdates

LOCAL_SRC_FILES := $(call all-java-files-under,src)

LOCAL_PROGUARD_FLAG_FILES := proguard.cfg

LOCAL_AAPT_INCLUDE_ALL_RESOURCES := true

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
	libs/android-support-v4.jar \
	libs/RootTools.jar

LOCAL_AAPT_INCLUDE_ALL_RESOURCES := true

include $(BUILD_PACKAGE)

include $(BUILD_MULTI_PREBUILT)
##