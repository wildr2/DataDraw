package datadraw;
import processing.core.*;
import processing.core.PApplet;



public class Helper
{
	public static int RandomSign(PApplet p)
	{
		return p.random(0, 1) > 0.5f ? 1 : -1;
	}
	public static boolean RandomBool(PApplet p)
	{
		return p.random(0, 1) > 0.5f ? true : false;
	}
	public static boolean RandomBool(PApplet p, float chance_true)
	{
		return p.random(0, 1) > chance_true ? false : true;
	}
	public static float FloatSeconds(PApplet p)
	{
		return p.millis() / 1000f;
	}

	public static float Clamp(float value, float min, float max)
	{
		return value < min ? min : value > max ? max : value;
	}
	public static int Clamp(int value, int min, int max)
	{
		return value < min ? min : value > max ? max : value;
	}
	public static int WrapIndex(int i, int len)
	{
		return (i % len + len) % len;
	}
	public static PVector Normalize(PVector p)
	{
		float m = (float)Math.sqrt(p.x * p.x + p.y * p.y);
		PVector p_new = new PVector(p.x, p.y);
		p_new.x /= m;
		p_new.y /= m;
		
		return p_new;
	}
	public static boolean PointInCircle(PVector pos, float r, PVector p)
	{
		return PVector.dist(pos, p) < r;
	}
	public static boolean PointInRect(PVector pos, float w, float h, PVector p)
	{
		if (p.x > pos.x && p.x < pos.x + w)
			if (p.y > pos.y && p.y < pos.y + h)
				return true;
		
		return false;
	}
	
}

