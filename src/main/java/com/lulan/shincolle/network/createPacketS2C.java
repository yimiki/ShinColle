package com.lulan.shincolle.network;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import com.lulan.shincolle.entity.BasicEntityShip;
import com.lulan.shincolle.proxy.CommonProxy;
import com.lulan.shincolle.reference.AttrID;
import com.lulan.shincolle.reference.Names;
import com.lulan.shincolle.utility.LogHelper;

import net.minecraft.entity.Entity;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

//create server packet by Jabelar
//web: jabelarminecraft.blogspot.tw/p/packet-handling-for-minecraft-forge-172.html
public class createPacketS2C {
 
	public createPacketS2C() {
	}

	/**ENTITY SYNC PACKET
	 * �Ω�P�Bserver��client��entity���
	 * Format: PacketID + EntityID + ShipLevel + Kills + 
	 *         AttrBonus[] + AttrFinal[] + EntityState[] + BonusPoint[]
	 * 
	 */
	public static FMLProxyPacket createEntitySyncPacket(Entity parEntity) throws IOException {
		//�إ�packet�ǿ�stream
		ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());
		
		//Packet ID (�|��b�ʥ]�Y�H���ѫʥ]����)
		bbos.writeByte(Names.Packets.ENTITY_SYNC);
		//Entity ID (�Ω����entity�O���@��)
		bbos.writeInt(parEntity.getEntityId());
		//�H�U�g�J�n�ǰe�����
		if (parEntity instanceof BasicEntityShip) {
			BasicEntityShip entity = (BasicEntityShip)parEntity;
			bbos.writeShort(entity.ShipLevel);
			bbos.writeInt(entity.Kills);
			
			bbos.writeShort(entity.AttrEquipShort[AttrID.HP]);
			bbos.writeShort(entity.AttrEquipShort[AttrID.ATK]);
			bbos.writeShort(entity.AttrEquipShort[AttrID.DEF]);
			bbos.writeFloat(entity.AttrEquipFloat[AttrID.SPD]);
			bbos.writeFloat(entity.AttrEquipFloat[AttrID.MOV]);
			bbos.writeFloat(entity.AttrEquipFloat[AttrID.HIT]);
			
			bbos.writeShort(entity.AttrFinalShort[AttrID.HP]);
			bbos.writeShort(entity.AttrFinalShort[AttrID.ATK]);
			bbos.writeShort(entity.AttrFinalShort[AttrID.DEF]);
			bbos.writeFloat(entity.AttrFinalFloat[AttrID.SPD]);
			bbos.writeFloat(entity.AttrFinalFloat[AttrID.MOV]);
			bbos.writeFloat(entity.AttrFinalFloat[AttrID.HIT]);
			
			bbos.writeByte(entity.EntityState[AttrID.State]);
			bbos.writeByte(entity.EntityState[AttrID.Emotion]);
			bbos.writeByte(entity.EntityState[AttrID.SwimType]);
			
			bbos.writeByte(entity.BonusPoint[0]);
			bbos.writeByte(entity.BonusPoint[1]);
			bbos.writeByte(entity.BonusPoint[2]);
			bbos.writeByte(entity.BonusPoint[3]);
			bbos.writeByte(entity.BonusPoint[4]);
			bbos.writeByte(entity.BonusPoint[5]);			
		}

		// put payload into a packet  
		FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), CommonProxy.channelName);
		// don't forget to close stream to avoid memory leak
		bbos.close();
  
		return thePacket;
	}
	
	/**ATTACK PARTICLE(SMALL) PACKET
	 * �o�e�S�īʥ], �ϳQ������entity�o�Xparticle, ���q�����A��
	 * Format: PacketID + TargetEntityID + ParticleID
	 */
	public static FMLProxyPacket createAttackSmallParticlePacket(Entity target, int type) throws IOException {
		//�إ�packet�ǿ�stream
		ByteBufOutputStream bbos = new ByteBufOutputStream(Unpooled.buffer());
		
		//Packet ID (�|��b�ʥ]�Y�H���ѫʥ]����)
		bbos.writeByte(Names.Packets.PARTICLE_ATK);
		//Entity ID (�Ω����entity�O���@��)
		bbos.writeInt(target.getEntityId());
		//�H�U�g�J�n�ǰe�����
		bbos.writeByte((byte)type);

		// put payload into a packet  
		FMLProxyPacket thePacket = new FMLProxyPacket(bbos.buffer(), CommonProxy.channelName);
		// don't forget to close stream to avoid memory leak
		bbos.close();
  
		return thePacket;
	}
 
	//send to all player on the server
	public static void sendToAll(FMLProxyPacket parPacket) {
      CommonProxy.channel.sendToAll(parPacket);
	}

	//send entity sync packet
	public static void sendS2CEntitySync(Entity parEntity) {
    	try {
    		sendToAll(createEntitySyncPacket(parEntity));
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	//send attack particle packet
	public static void sendS2CAttackParticle(Entity parEntity, int type) {
    	try {
    		sendToAll(createAttackSmallParticlePacket(parEntity, type));
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
}