LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libvp9dec_sw
LOCAL_SRC_FILES_32 := arm/libvp9dec_sw.a
LOCAL_MULTILIB := 32
LOCAL_MODULE_CLASS := STATIC_LIBRARIES
LOCAL_MODULE_SUFFIX := .a
include $(BUILD_PREBUILT)
