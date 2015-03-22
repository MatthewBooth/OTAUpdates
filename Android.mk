#	Copyright (C) 2015 Matt Booth (Kryten2k35).
# 
# 	Licensed under the Attribution-NonCommercial-ShareAlike 4.0 International 
# 	(the "License") you may not use this file except in compliance with the License.
# 	You may obtain a copy of the License at
# 
# 		http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
# 
# 	Unless required by applicable law or agreed to in writing, software
# 	distributed under the License is distributed on an "AS IS" BASIS,
#	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#	See the License for the specific language governing permissions and
#	limitations under the License.

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

bypass_dir := ../../../external/bypass/platform/android/library
src_dirs := src $(bypass_dir)/src

LOCAL_SRC_FILES := $(call all-java-files-under, $(src_dirs))

google_play_dir := ../../../external/google/google_play_services/libproject/google-play-services_lib/res
res_dir := $(google_play_dir) res

LOCAL_PACKAGE_NAME := OTAUpdates
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true
LOCAL_PROGUARD_ENABLED := disabled

LOCAL_STATIC_JAVA_LIBRARIES := \
	RootTools \
	android-support-v4 \
	android-support-v7-cardview \
	play 

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, $(res_dir)) \
	frameworks/support/v7/cardview/res

LOCAL_AAPT_FLAGS := \
	--auto-add-overlay \
	--extra-packages com.google.android.gms

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
libs/RootTools.jar \

include $(BUILD_MULTI_PREBUILT)
