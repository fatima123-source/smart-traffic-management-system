package com.traffic.camera;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CameraRemote extends Remote {
    void detectAccident(int routeId, String description) throws RemoteException;
}