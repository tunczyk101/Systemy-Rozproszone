// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculator.proto

package sr.grpc.gen;

/**
 * Protobuf enum {@code calculator.ExistingType}
 */
public enum ExistingType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>MAGICIAN = 0;</code>
   */
  MAGICIAN(0),
  /**
   * <code>ADD = 1;</code>
   */
  ADD(1),
  /**
   * <code>SUB = 2;</code>
   */
  SUB(2),
  /**
   * <code>AV = 3;</code>
   */
  AV(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>MAGICIAN = 0;</code>
   */
  public static final int MAGICIAN_VALUE = 0;
  /**
   * <code>ADD = 1;</code>
   */
  public static final int ADD_VALUE = 1;
  /**
   * <code>SUB = 2;</code>
   */
  public static final int SUB_VALUE = 2;
  /**
   * <code>AV = 3;</code>
   */
  public static final int AV_VALUE = 3;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static ExistingType valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static ExistingType forNumber(int value) {
    switch (value) {
      case 0: return MAGICIAN;
      case 1: return ADD;
      case 2: return SUB;
      case 3: return AV;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<ExistingType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      ExistingType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<ExistingType>() {
          public ExistingType findValueByNumber(int number) {
            return ExistingType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return sr.grpc.gen.CalculatorProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final ExistingType[] VALUES = values();

  public static ExistingType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private ExistingType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:calculator.ExistingType)
}

