package com.zbq.library.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zbq.library.bean.Request;
import com.zbq.library.bean.Response;


public interface IProcessCallback extends IInterface {


    abstract class Stub extends Binder implements IProcessCallback{
        private static final String DESCRIPTOR = "com.zbq.library.internal.IProcessCallback";
        public Stub(){
            this.attachInterface(this,DESCRIPTOR);
        }

        public static IProcessCallback asInterface(IBinder obj){
            if ((obj == null)){
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if ((iin != null)&&( iin instanceof IProcessCallback)){
                return (IProcessCallback) iin;
            }
            return new Proxy(obj);
        }


        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
                case TRANSACTION_callback:
                    data.enforceInterface(DESCRIPTOR);
                    Request _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = Request.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Response _result = this.callback(_arg0);
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

         @Override
         public IBinder asBinder() {
             return this;
         }


        private static class Proxy implements IProcessCallback{
            private IBinder mRemote;

            Proxy(IBinder remote){
                mRemote = remote;
            }
            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }
            @Override
            public IBinder asBinder() {
                return mRemote;
            }
            @Override
            public Response callback(Request request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Response _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((request!=null)) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_callback, _data, _reply, 0);
                    _reply.readException();
                    if ((0!=_reply.readInt())) {
                        _result = Response.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

        }
        static final int TRANSACTION_callback = IBinder.FIRST_CALL_TRANSACTION;
     }

    Response callback(Request request) throws RemoteException;
}
