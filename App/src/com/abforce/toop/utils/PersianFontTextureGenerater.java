package com.abforce.toop.utils;

import java.util.regex.Pattern;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.abforce.toop.managers.RM;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class PersianFontTextureGenerater {

	public static final int WIDTH = 220;
	public static final int HEIGHT = 80;
	
	public static ITextureRegion getTextureFor(final String text){
		final Typeface font = Typeface.createFromAsset(RM.STATE.activity.getAssets(), "fonts/homa.ttf");
		
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(RM.STATE.engine.getTextureManager(), WIDTH, HEIGHT, TextureOptions.BILINEAR);
        ITextureRegion mDecoratedBalloonTextureRegion;
        final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource(WIDTH, HEIGHT);
        final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
                @Override
                protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
                        this.mPaint.setColor(Color.rgb(220, 220, 255));
                        this.mPaint.setStyle(Style.FILL);
                        this.mPaint.setTextSize(22f);
                        this.mPaint.setTextAlign(Align.CENTER);
                        this.mPaint.setAntiAlias(true);
                        this.mPaint.setTypeface(font);
                        
                        String[] lines = text.split(Pattern.quote("\n"));
                        float step = HEIGHT / (lines.length + 1); 
                        for(int i = 0; i < lines.length; i += 1){
                        	pCanvas.drawText(lines[i], WIDTH / 2, (i + 1 ) * step, this.mPaint);
                        }
                }

                @Override
                public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
                	return null;
                }
        };

        mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
        mBitmapTextureAtlas.load();
        
        return mDecoratedBalloonTextureRegion;
	}
	
}
