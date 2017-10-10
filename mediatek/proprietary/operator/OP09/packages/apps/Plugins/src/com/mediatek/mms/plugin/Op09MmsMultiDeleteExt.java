/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2012. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.mediatek.mms.plugin;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Telephony.Sms;

import com.mediatek.common.PluginImpl;
import com.mediatek.mms.ext.DefaultMmsMultiDeleteExt;
import com.mediatek.xlog.Xlog;

/**
 * M: Op09MmsMultiDeleteExt.
 */
@PluginImpl(interfaceName = "com.mediatek.mms.ext.IMmsMultiDeleteExt")
public class Op09MmsMultiDeleteExt extends DefaultMmsMultiDeleteExt {
    private static final String TAG = "Mms/Op09MmsComposeExt";

    /**
     * The Constructor.
     * @param context the Context.
     */
    public Op09MmsMultiDeleteExt(Context context) {
        super(context);
    }

    @Override
    public boolean deleteMassTextMsg(String[] msgIds) {
        this.getHost().deleteMassTextInHost(msgIds);
        Xlog.d(TAG, "deleteMassTextMsg in Plugin method");
        return true;
    }

    @Override
    public boolean lockMassTextMsgs(final Context context, long msgIds[], boolean locked) {
        if (context == null || msgIds == null || msgIds.length < 1) {
            return false;
        }
        StringBuffer idBuffer = new StringBuffer();
        for (long id : msgIds) {
            idBuffer.append(id + ",");
        }
        final String ids = idBuffer.substring(0, idBuffer.length() - 1);
        final ContentValues values = new ContentValues(1);
        values.put("locked", locked ? 1 : 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver().update(
                    Sms.CONTENT_URI,
                    values,
                    "(ipmsg_id in ( select distinct ipmsg_id from sms where ipmsg_id < 0 "
                        + " and _id in (" + ids + " ))) or ( _id in ( " + ids
                        + " ) and ipmsg_id >= 0 )", null);
            }
        }).start();
        return true;
    }
}
