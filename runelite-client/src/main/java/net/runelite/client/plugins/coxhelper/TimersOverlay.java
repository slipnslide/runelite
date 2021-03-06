package net.runelite.client.plugins.coxhelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class TimersOverlay extends Overlay
{

	private CoxPlugin plugin;
	private CoxConfig config;
	private Client client;
	private Color tickcolor = new Color(255, 255, 255, 255);

	@Inject
	TimersOverlay(CoxPlugin plugin, CoxConfig config, Client client)
	{
		this.plugin = plugin;
		this.config = config;
		this.client = client;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGHEST);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.timers())
		{
			if (plugin.getBurnTarget().size() > 0)
			{
				for (Actor actor : plugin.getBurnTarget())
				{
					renderNpcOverlay(graphics, actor, new Color(255, 100, 0, 255), 2, 100, 10);
					final int ticksLeft = plugin.getBurnTicks();
					String ticksLeftStr = String.valueOf(ticksLeft);
					if (ticksLeft >= 0)
					{
						if (ticksLeft == 34 ||
							ticksLeft == 33 ||
							ticksLeft == 26 ||
							ticksLeft == 25 ||
							ticksLeft == 18 ||
							ticksLeft == 17 ||
							ticksLeft == 10 ||
							ticksLeft == 9 ||
							ticksLeft == 2 ||
							ticksLeft == 1)
						{
							tickcolor = new Color(255, 0, 0, 255);
							ticksLeftStr = "GAP";
						}
						else
						{
							tickcolor = new Color(255, 255, 255, 255);
						}
						Point canvasPoint = actor.getCanvasTextLocation(graphics, ticksLeftStr, 0);
						renderTextLocation(graphics, ticksLeftStr, 14, Font.BOLD, tickcolor, canvasPoint);
					}
				}
			}

			if (plugin.getAcidTarget() != null)
			{
				Actor actor = plugin.getAcidTarget();
				renderNpcOverlay(graphics, actor, new Color(69, 241, 44, 255), 2, 100, 10);
				final int ticksLeft = plugin.getAcidTicks();
				if (ticksLeft > 0)
				{
					if (ticksLeft > 1)
					{
						tickcolor = new Color(69, 241, 44, 255);
					}
					else
					{
						tickcolor = new Color(255, 255, 255, 255);
					}
					final String ticksLeftStr = String.valueOf(ticksLeft);
					Point canvasPoint = actor.getCanvasTextLocation(graphics, ticksLeftStr, 0);
					renderTextLocation(graphics, ticksLeftStr, 14, Font.BOLD, tickcolor, canvasPoint);
				}
			}
		}

		if (config.tpOverlay())
		{
			if (plugin.getTeleportTarget().size() > 0)
			{
				for (Actor actor : plugin.getTeleportTarget())
				{
					renderNpcOverlay(graphics, actor, new Color(193, 255, 245, 255), 2, 100, 10);
				}
			}
		}

		return null;
	}

	private void renderNpcOverlay(Graphics2D graphics, Actor actor, Color color, int outlineWidth, int outlineAlpha, int fillAlpha)
	{
		int size = 1;
		LocalPoint lp = actor.getLocalLocation();
		Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);

		if (tilePoly != null)
		{
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
			graphics.setStroke(new BasicStroke(outlineWidth));
			graphics.draw(tilePoly);
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
			graphics.fill(tilePoly);
		}
	}

	private void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint)
	{
		graphics.setFont(new Font("Arial", fontStyle, fontSize));
		if (canvasPoint != null)
		{
			final Point canvasCenterPoint = new Point(
				canvasPoint.getX(),
				canvasPoint.getY());
			final Point canvasCenterPoint_shadow = new Point(
				canvasPoint.getX() + 1,
				canvasPoint.getY() + 1);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
		}
	}
}
