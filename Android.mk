LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-subdir-java-files) $(call all-renderscript-files-under, src)

cards_res := ../../../frameworks/support/v7/cardview/res
res_dirs := $(cards_res) res

LOCAL_PACKAGE_NAME := OTAUpdates
LOCAL_CERTIFICATE := shared

LOCAL_STATIC_JAVA_LIBRARIES := \
	RootTools \
	android-support-v4 \
	android-support-v7-cardview

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, $(res_dirs))
LOCAL_AAPT_FLAGS := --auto-add-overlay

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
libs/RootTools.jar \

include $(BUILD_MULTI_PREBUILT)