package com.zbq.library.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zbq.library.model.Request;
import com.zbq.library.model.Response;


public interface IProcessService extends IInterface {


   abstract class Stub extends Binder implements IProcessService{

        private static final String DESCRIPTOR = "com.zbq.library.internal.IProcessService";

        public Stub(){
            this.attachInterface(this,DESCRIPTOR);
        }

        public static IProcessService asInterface(IBinder obj){
            if ((obj == null)){
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if ((iin != null)&&( iin instanceof IProcessService)){
                return (IProcessService) iin;
            }
            return new Proxy(obj);
        }

       @Override
       public IBinder asBinder() {
           return this;
       }

       @Override
       protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
           switch (code) {
               case INTERFACE_TRANSACTION:
                   reply.writeString(DESCRIPTOR);
                   return true;
               case TRANSACTION_send:
                   data.enforceInterface(DESCRIPTOR);
                   Request _arg0;
                   if ((0 != data.readInt())) {
                       _arg0 = Request.CREATOR.createFromParcel(data);
                   } else {
                       _arg0 = null;
                   }
                   Response _result = this.send(_arg0);
                   reply.writeNoException();
                   if ((_result != null)) {
                       reply.writeInt(1);
                       _result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                   } else {
                       reply.writeInt(0);
                   }
                   return true;
               case TRANSACTION_register:
                   data.enforceInterface(DESCRIPTOR);
                   IProcessCallback _arg1;
                   IBinder iBinder = data.readStrongBinder();
                   _arg1 = IProcessCallback.Stub.asInterface(iBinder);
                   int pid = data.readInt();
                   this.register(_arg1, pid);
                   reply.writeNoException();
                   return true;
           }
           return super.onTransact(code, data, reply, flags);
       }


       private static class Proxy implements IProcessService{
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
           public void register(IProcessCallback callback, int pid) throws RemoteException {

               Parcel _data = Parcel.obtain();
               Parcel _reply = Parcel.obtain();
               try {
                   _data.writeInterfaceToken(DESCRIPTOR);
                   _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
                   _data.writeInt(pid);
                   mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
                   _reply.readException();
               } finally {
                   _reply.recycle();
                   _data.recycle();
               }
           }

           @Override
           public Response send(Request request) throws RemoteException {
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
                   mRemote.transact(Stub.TRANSACTION_send, _data, _reply, 0);
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

       static final int TRANSACTION_send = IBinder.FIRST_CALL_TRANSACTION;
       static final int TRANSACTION_register = IBinder.FIRST_CALL_TRANSACTION + 1;
    }

    void register(IProcessCallback callback, int pid) throws RemoteException;


    Response send(Request request) throws RemoteException;
}
