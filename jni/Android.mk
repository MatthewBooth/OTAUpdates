LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE	:= libbypass
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
	android/bypass.cpp \
	src/parser.cpp \
	src/document.cpp \
	src/element.cpp \
	dep/libsoldout/markdown.c \
	dep/libsoldout/buffer.c \
	dep/libsoldout/array.c

LOCAL_SHARED_LIBRARIES := libcutils libdl libc++
	
LOCAL_C_INCLUDES := $(LOCAL_PATH)/android \
		    $(LOCAL_PATH)/src \
		    $(LOCAL_PATH)/dep/libsoldout \
		    $(LOCAL_PATH)/boost_1_57_0 \
		    external/libcxx/include

include $(BUILD_SHARED_LIBRARY)
