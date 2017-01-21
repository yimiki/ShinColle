package com.lulan.shincolle.client.particle;

import org.lwjgl.opengl.GL11;

import com.lulan.shincolle.proxy.ClientProxy;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * CUSTOM TEXT PARTICLE
 * 顯示任意文字在目標上
 */
@SideOnly(Side.CLIENT)
public class ParticleTextsCustom extends Particle
{

	private int particleType, textWidth, textHeight;
	private RenderManager rm;
	private FontRenderer fr;
	private String text;
	
	
    public ParticleTextsCustom(World world, double posX, double posY, double posZ, float scale, int type, String text, int...parms)
    {
        super(world, 0D, 0D, 0D);
        this.setSize(0F, 0F);
        this.setPosition(posX, posY, posZ);
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.motionX = 0D;
        this.motionY = 0D;
        this.motionZ = 0D;
        this.particleScale = scale;
        this.particleMaxAge = 30;
        this.particleType = type;
        this.canCollide = false;	//can clip = false
        
        this.rm = ClientProxy.getMineraft().getRenderManager();
        this.fr = rm.getFontRenderer();
        
        switch (type)
        {
        case 0:	//draw string with specific #lines and width
        	this.textHeight = parms[0] - 1;
        	this.textWidth = parms[1] / 2;
        	this.text = text;
    	break;
        }
    }

    @Override
    public void renderParticle(VertexBuffer render, Entity entity, float ptick, float cosYaw, float cosPitch, float sinYaw, float sinYawsinPitch, float cosYawsinPitch)
    {
        float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * ptick - interpPosX);
        float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * ptick - interpPosY);
        float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ptick - interpPosZ);
        
    	GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0F, 1F, 0F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.rotate(-rm.playerViewY, 0F, 1F, 0F);
        GlStateManager.rotate(rm.playerViewX, 1F, 0F, 0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        GlStateManager.disableTexture2D();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        render.pos((double)(-this.textWidth - 1), -1D, 0D).color(0F, 0F, 0F, 0.25F).endVertex();
        render.pos((double)(-this.textWidth - 1), 8D + this.textHeight * 9D, 0D).color(0F, 0F, 0F, 0.25F).endVertex();
        render.pos((double)(this.textWidth + 1), 8D + this.textHeight * 9D, 0D).color(0F, 0F, 0F, 0.25F).endVertex();
        render.pos((double)(this.textWidth + 1), -1D, 0D).color(0F, 0F, 0F, 0.25F).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();

        //draw text
        GlStateManager.depthMask(true);
        this.fr.drawSplitString(this.text, -this.textWidth, 0, this.textWidth * 2, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }
    
    //layer: 0:particle 1:terrain 2:items 3:custom?
    @Override
    public int getFXLayer()
    {
        return 3;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ > this.particleMaxAge)
        {
            this.setExpired();
        }
    }
    
    
}