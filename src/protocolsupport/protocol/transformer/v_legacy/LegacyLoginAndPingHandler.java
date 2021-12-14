package protocolsupport.protocol.transformer.v_legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.PacketDataSerializer;
import protocolsupport.protocol.RecyclablePacketDataSerializer;

@Sharable
public class LegacyLoginAndPingHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf input) throws Exception {
		RecyclablePacketDataSerializer serializer = RecyclablePacketDataSerializer.create(ProtocolVersion.MINECRAFT_LEGACY);
		try {
			int packetId = input.readUnsignedByte();
			if (packetId == 0x02 || packetId == 0xFC) {
				writeLoginKick(serializer);
			}
			else {
				throw new DecoderException("Unknown packet id "+packetId + " in legacy login and ping handler");
			}
			ctx.channel().pipeline().firstContext().writeAndFlush(serializer).addListener(ChannelFutureListener.CLOSE);
		} finally {
			serializer.release();
		}
	}

	private static void writeLoginKick(PacketDataSerializer serializer) {
		serializer.writeByte(0xFF);
		serializer.writeString("Outdated client!");
	}

}
