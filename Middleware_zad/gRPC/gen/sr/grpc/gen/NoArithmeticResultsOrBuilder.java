// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculator.proto

package sr.grpc.gen;

public interface NoArithmeticResultsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:calculator.NoArithmeticResults)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string info = 1;</code>
   * @return The info.
   */
  java.lang.String getInfo();
  /**
   * <code>string info = 1;</code>
   * @return The bytes for info.
   */
  com.google.protobuf.ByteString
      getInfoBytes();

  /**
   * <code>int32 result = 2;</code>
   * @return The result.
   */
  int getResult();

  /**
   * <code>.calculator.ExistingType otherType = 3;</code>
   * @return The enum numeric value on the wire for otherType.
   */
  int getOtherTypeValue();
  /**
   * <code>.calculator.ExistingType otherType = 3;</code>
   * @return The otherType.
   */
  sr.grpc.gen.ExistingType getOtherType();
}