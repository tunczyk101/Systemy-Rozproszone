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

public class AlreadyOffError extends com.zeroc.Ice.UserException
{
    public AlreadyOffError()
    {
    }

    public AlreadyOffError(Throwable cause)
    {
        super(cause);
    }

    public String ice_id()
    {
        return "::SmartHomeDevices::AlreadyOffError";
    }

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::SmartHomeDevices::AlreadyOffError", -1, true);
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
    public static final long serialVersionUID = -937015637L;
}
