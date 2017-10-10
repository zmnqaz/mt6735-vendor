# Copyright Statement:
#
# This software/firmware and related documentation ("MediaTek Software") are
# protected under relevant copyright laws. The information contained herein
# is confidential and proprietary to MediaTek Inc. and/or its licensors.
# Without the prior written permission of MediaTek inc. and/or its licensors,
# any reproduction, modification, use or disclosure of MediaTek Software,
# and information contained herein, in whole or in part, shall be strictly prohibited.
#
# MediaTek Inc. (C) 2010. All rights reserved.
#
# BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
# THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
# RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
# AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
# NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
# SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
# SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
# THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
# THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
# CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
# SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
# STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
# CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
# AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
# OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
# MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
#
# The following software/firmware and/or related documentation ("MediaTek Software")
# have been modified by MediaTek Inc. All revisions are subject to any receiver's
# applicable license agreements with MediaTek Inc.
#===============================================================================


LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

ifeq ($(BUILD_MTK_LDVT),true)
    LOCAL_CFLAGS += -DUSING_MTK_LDVT
endif

LOCAL_SRC_FILES += \
    flicker_hal.cpp flicker_util.cpp flicker_hal_base.cpp

LOCAL_C_INCLUDES:= \
    $(TOP)/bionic \
    $(TOP)/external/stlport/stlport \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/include/D2/mtkcam/algorithm/libflicker \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/include/D2/mtkcam/featureio \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/inc/common \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2 \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/imageio_common/inc \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/inc/featureio \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/drv/imgsensor \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/inc/drv \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/hal/sensor \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc/aaa \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc/isp_tuning \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/isp_mgr \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/nvram_mgr \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/isp_tuning \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/sensor_mgr \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/ae_mgr \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/af_mgr \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/drv/inc \
    $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/D2/core/featureio/pipe/aaa/ispdrv_mgr \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/inc/aaa \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/inc/isp_tuning \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/camera/inc \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/inc \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/inc/mtkcam \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/inc/debug_exif/aaa \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/hal/D2/imgsensor \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/cgen/cfgfileinc \
    $(TOP)/$(MTK_PATH_CUSTOM_PLATFORM)/kernel/imgsensor/inc \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc/aaa \
    $(TOP)/mediatek/custom/common/kernel/imgsensor/inc \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/camera/inc \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/inc/debug_exif/aaa \
    $(TOP)/$(MTK_PATH_CUSTOM)/hal/imgsensor \
    $(TOP)/$(MTK_PATH_CUSTOM)/cgen/cfgfileinc \
    $(TOP)/$(MTK_PATH_COMMON)/kernel/imgsensor/inc \

LOCAL_C_INCLUDES += $(TOP)/$(MTK_PATH_SOURCE)/hardware/mtkcam/inc/featureio
LOCAL_C_INCLUDES += $(TOP)/$(MTK_PATH_SOURCE)/hardware/mtkcam/inc/drv


#LOCAL_MULTILIB := 32


LOCAL_MODULE := libfeatureiopipe_flk


#
# Start of common part ------------------------------------
sinclude $(TOP)/$(MTK_PATH_PLATFORM)/hardware/mtkcam/mtkcam.mk

#-----------------------------------------------------------
LOCAL_CFLAGS += $(MTKCAM_CFLAGS)

#-----------------------------------------------------------
LOCAL_C_INCLUDES += $(MTKCAM_C_INCLUDES)

#-----------------------------------------------------------
LOCAL_C_INCLUDES += $(TOP)/$(MTK_PATH_SOURCE)/hardware/include
LOCAL_C_INCLUDES += $(TOP)/$(MTK_PATH_PLATFORM)/hardware/include/D2

# End of common part ---------------------------------------
#
include $(BUILD_STATIC_LIBRARY)

