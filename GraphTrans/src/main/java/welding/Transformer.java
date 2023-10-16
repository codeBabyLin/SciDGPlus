package welding;

import Common.BaseSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

public class Transformer {

    private ByteBufAllocator allocator;


    private ByteBuf byteBuf;

    public Transformer(){
        this.allocator  = ByteBufAllocator.DEFAULT;
        this.byteBuf = allocator.heapBuffer();
    }

    public byte[] LongToByte(Long l){
        //ByteBuf byteBuf  = allocator.heapBuffer();
        byteBuf.clear();
        byteBuf.writeLong(l);
        byte[] data= new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }
    public Long ByteToLong(byte[] data){
        ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
        Long d = byteBuf.readLong();
        return d;
    }
    public byte[] StringToByte(String s){
        return s.getBytes();
    }
    public String byteToString(byte[] data){
        return new String(data);
    }

 /*  public byte[] NodeToByte(Node node){
       ByteBuf byteBuf  = allocator.heapBuffer();
       byteBuf.writeLong((Long)node);
       byte[] data= new byte[byteBuf.writerIndex()];
       byteBuf.readBytes(data);
      return data;
   }
   public Node ByteToNode(byte[] data){
       ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
       Long d = (Long)byteBuf.readLong();
       return (Node)d;
   }*/

   /* public byte[] RelationToByte(Relation relation){
        ByteBuf byteBuf  = allocator.heapBuffer();
        byteBuf.writeLong((Long)relation);
        byte[] data= new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }
    public Relation ByteToRelation(byte[] data){
        ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
        Long d = (Long)byteBuf.readLong();
        return (Relation)d;
    }*/
   public byte[] VersionToByte(long version[]){
        //ByteBuf byteBuf  = allocator.heapBuffer();
       byteBuf.clear();
        byteBuf.writeLong(version[0]);
        byteBuf.writeLong(version[1]);
        byte[] data= new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }
    public long[] ByteToVersion(byte[] data){
        ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
        long v1 = byteBuf.readLong();
        long v2 = byteBuf.readLong();
        return new long[]{v1,v2};
    }

    public long readTimeFromPropertyKey(byte[]data){
        ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
        long v1 = byteBuf.readLong();
        long v2 = byteBuf.readLong();
        long v3 = byteBuf.readLong();
        return v3;
    }

    public byte[] objectToByte(Object o){
        byteBuf.clear();
        BaseSerializer._writeAny(o,byteBuf);
        byte[]data = new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }
    public Object byteToObject(byte[] data){
        ByteBuf byteBuf  = Unpooled.copiedBuffer(data);
        Object value = BaseSerializer._readAny(byteBuf);
        return value;
    }

    public byte[] concatByteArray(byte[]data1,byte[]data2){
        byteBuf.clear();
        byteBuf.writeBytes(data1);
        byteBuf.writeBytes(data2);
        byte[] data= new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }
    public byte[] concatByteArray(byte[]data1,byte[]data2,byte[]data3){
        byteBuf.clear();
        byteBuf.writeBytes(data1);
        byteBuf.writeBytes(data2);
        byteBuf.writeBytes(data3);
        byte[] data= new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(data);
        return data;
    }


}
