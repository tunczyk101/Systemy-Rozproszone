//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.9
//
// <auto-generated>
//
// Generated from file `smarthome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHomeDevices;

public class PanTiltZoomError extends com.zeroc.Ice.UserException
{
    public PanTiltZoomError()
    {
    }

    public PanTiltZoomError(Throwable cause)
    {
        super(cause);
    }

    public String ice_id()
    {
        return "::SmartHomeDevices::PanTiltZoomError";
    }

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::SmartHomeDevices::PanTiltZoomError", -1, true);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = -1091005649L;
}