LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-subdir-java-files) $(call all-renderscript-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
	RootTools \
	android-support-v4

LOCAL_PACKAGE_NAME := OTAUpdates
LOCAL_CERTIFICATE := shared

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
libs/RootTools.jar \
libs/android-support-v4.jar

include $(BUILD_MULTI_PREBUILT)