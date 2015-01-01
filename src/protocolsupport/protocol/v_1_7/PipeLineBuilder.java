package protocolsupport.protocol.v_1_7;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.NetworkManager;
import net.minecraft.server.v1_8_R1.PacketPrepender;
import net.minecraft.server.v1_8_R1.PacketSplitter;
import protocolsupport.protocol.IPipeLineBuilder;
import protocolsupport.protocol.fake.FakeDecoder;
import protocolsupport.protocol.fake.FakeEncoder;
import protocolsupport.protocol.fake.FakePrepender;
import protocolsupport.protocol.fake.FakeSplitter;
import protocolsupport.protocol.v_1_7.clientboundtransformer.PacketEncoder;
import protocolsupport.protocol.v_1_7.serverboundtransformer.PacketDecoder;

public class PipeLineBuilder implements IPipeLineBuilder {

	public DecoderEncoderTuple buildPipeLine(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.channel().pipeline();
		NetworkManager networkmanager = pipeline.get(NetworkManager.class);
		networkmanager.a(new HandshakeListener(MinecraftServer.getServer(), networkmanager));
		ChannelHandler decoder = new PacketDecoder();
		ChannelHandler encoder = new PacketEncoder();
		pipeline.replace(FakeSplitter.class, "splitter", new PacketSplitter());
		pipeline.replace(FakeDecoder.class, "decoder", decoder);
		pipeline.replace(FakePrepender.class, "prepender", new PacketPrepender());
		pipeline.replace(FakeEncoder.class, "encoder", encoder);
		return new DecoderEncoderTuple(decoder, encoder);
	}

}
