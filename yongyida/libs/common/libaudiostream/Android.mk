LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libaudiostream
LOCAL_SRC_FILES_32 := arm/libaudiostream.a
LOCAL_MULTILIB := 32
LOCAL_MODULE_CLASS := STATIC_LIBRARIES
LOCAL_MODULE_SUFFIX := .a
include $(BUILD_PREBUILT)
