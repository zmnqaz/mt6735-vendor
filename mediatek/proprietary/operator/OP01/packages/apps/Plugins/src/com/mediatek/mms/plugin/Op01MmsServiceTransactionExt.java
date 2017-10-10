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

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemProperties;
import android.provider.Settings;

import com.mediatek.mms.service.ext.DefaultMmsServiceTransactionExt;
import com.mediatek.xlog.Xlog;

/// M: ALPS00440523, set service to foreground @ {
import android.app.Service;
/// @}
/// M: ALPS00452618, set special HTTP retry handler for CMCC FT @
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
/// @}

/// M: ALPS00545779, for FT, restart pending receiving mms @ {
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
/// @}
import android.telephony.TelephonyManager;

import com.mediatek.common.PluginImpl;

@PluginImpl(interfaceName="com.mediatek.mms.service.ext.IMmsServiceTransactionExt")
public class Op01MmsServiceTransactionExt extends DefaultMmsServiceTransactionExt {
    private static final String TAG = "Mms/Op01MmsServiceTransactionExt";

    private static final int sMaxFailTime = 3;
    private static final int sSC504 = 504;

    private Context mContext = null;
    private int mServerFailCount = 0;
    private long mLastSimId = -1;

    private static final int SOCKET_TIMEOUT = 90 * 1000;
    private static final int SOCKET_SEND_TIMEOUT = 30 * 1000;
    
    public Op01MmsServiceTransactionExt(Context context) {
        super(context);
        mContext = context;
    }

    public synchronized void setMmsServerStatusCode(int code) {
        Xlog.d(TAG, "setMmsServerStatusCode, code=" + code);
        updateServerFailRecord(code);
    }

    /// M: set property @ {
    public void setSoSendTimeoutProperty() {
        Xlog.d(TAG, "setSoSendTimeoutProperty");
        System.setProperty("SO_SND_TIMEOUT", "1");
    }
    /// @}
    
    public void setSoSndTimeout(HttpParams params) {
        Xlog.d(TAG, "setSoSndTimeout");
        HttpConnectionParams.setSoSndTimeout(params, SOCKET_SEND_TIMEOUT);
        return;
    }
    
    
    public void setSocketTimeout(HttpParams params,int socketTimeout) {
    	Xlog.d(TAG, "op01 set socket timeout: " + SOCKET_TIMEOUT);
    	HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
    }


    private void updateServerFailRecord(int code) {
        Xlog.d(TAG, "updateServerFailRecord, code=" + code + ", count=" + mServerFailCount);
        if (!isConcernErrorCode(code)) {
            mServerFailCount = 0;
            return;
        }

        mServerFailCount++;
    }

    private boolean isConcernErrorCode(int code) {
        if (code >= 400 && code < 600) {
            return true;
        }
        return false;
    }
    
    public boolean isSyncPdpConnectedState() {
    	return true;
    }

    /// M: ALPS00452618, set special HTTP retry handler for CMCC FT @
    public DefaultHttpRequestRetryHandler getHttpRequestRetryHandler() {
        Xlog.d(TAG, "getHttpRequestRetryHandler");
        return new Op01HttpRequestRetryHandler(mContext, 1, true);
    }
    /// @}

    /// M: ALPS00440523, set service to foreground @
    /**
     * Set service to foreground
     *
     * @param service         Service that need to be foreground
     */
    public void startServiceForeground(Service service) {
        Xlog.d(TAG, "startServiceForeground");
        ///M: remove for ALPS01241119. for kk, notification will show on statusbar even icon id = 0. @{
        Notification noti = new Notification(0, null, System.currentTimeMillis());
        noti.flags |= Notification.FLAG_NO_CLEAR;
        noti.flags |= Notification.FLAG_HIDE_NOTIFICATION;
        if (service != null) {
            service.startForeground(1, noti);
        }
        /// @}
    }

    /**
     * Set service to foreground
     *
     * @param service         Service that need stop to be foreground
     */
    public void stopServiceForeground(Service service) {
        Xlog.d(TAG, "stopServiceForeground");
        ///M: remove for ALPS01241119.  @{
        if (service != null) {
            service.stopForeground(true);
        }
        /// @}
    }
    /// @}

    /// M: ALPS00545779, for FT, restart pending receiving mms @ {    
    public boolean isPendingMmsNeedRestart(Uri pduUri, int failureType) {
        Xlog.d(TAG, "isPendingMmsNeedRestart, uri=" + pduUri);

        final int PDU_COLUMN_STATUS = 2;
        final String[] PDU_PROJECTION = new String[] {
            Mms.MESSAGE_BOX,
            Mms.MESSAGE_ID,
            Mms.STATUS,
        };
        Cursor c = null;
        ContentResolver contentResolver = mContext.getContentResolver();

        try {
            c = contentResolver.query(pduUri, PDU_PROJECTION, null, null, null);

            if ((c == null) || (c.getCount() != 1) || !c.moveToFirst()) {
                Xlog.d(TAG, "Bad uri");
                return true;
            }

            int status = c.getInt(PDU_COLUMN_STATUS);
            Xlog.v(TAG, "status" + status);

            /* This notification is not processed yet, so need restart*/
            if (status == 0) {
                return true;
            }
            /* DEFERRED_MASK is not set, it is auto download*/
            if ((status & 0x04) == 0) {
                return isTransientFailure(failureType);
            }
            /* Reach here means it is manully download*/
            return false;
        } catch (SQLiteException e) {
            Xlog.e(TAG, "Catch a SQLiteException when query: ", e);
            return isTransientFailure(failureType);
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static boolean isTransientFailure(int type) {
        Xlog.d(TAG, "isTransientFailure, type=" + type);
        return (type < MmsSms.ERR_TYPE_GENERIC_PERMANENT);
    }
    /// @}
}


