package ru.sawim.models;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.MultiCallback;
import ru.sawim.comm.DrawableCallback;
import ru.sawim.icons.AniIcon;
import ru.sawim.icons.Icon;
import ru.sawim.modules.Emotions;

/**
 * Created with IntelliJ IDEA.
 * User: Gerc
 * Date: 13.05.13
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class SmilesAdapter extends BaseAdapter {

    private Context baseContext;
    Emotions emotions;
    private OnAdapterItemClickListener onItemClickListener;

    public SmilesAdapter(Context context) {
        baseContext = context;
        emotions = Emotions.instance;
    }

    @Override
    public int getCount() {
        return emotions.count();
    }

    @Override
    public Icon getItem(int i) {
        return emotions.getSmile(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convView, ViewGroup viewGroup) {
        ItemWrapper wr;
        if (convView == null) {
            convView = new ImageView(baseContext) {
                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(widthMeasureSpec));
                }
            };
            ((ImageView) convView).setScaleType(ImageView.ScaleType.CENTER);
            wr = new ItemWrapper((ImageView) convView);
            convView.setTag(wr);
        } else {
            wr = (ItemWrapper) convView.getTag();
        }
        if (emotions.isAniSmiles())
            wr.populateAniFrom((AniIcon) getItem(i));
        else
            wr.populateFrom(getItem(i));
        wr.click(i);
        return convView;
    }

//    private AnimationDrawable mAnimation = null;

//    private void stopFrameAnimation() {
//        if (mAnimation.isRunning()) {
//            mAnimation.stop();
//            mAnimation.setVisible(false, false);
//        }
//    }

    public void setOnItemClickListener(OnAdapterItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static abstract class OnAdapterItemClickListener implements AdapterView.OnItemClickListener {

        public abstract void onItemClick(SmilesAdapter adapter, int position);

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onItemClick((SmilesAdapter) parent.getAdapter(), position);
        }
    }

    public class ItemWrapper {
        private ImageView itemImage = null;

        public ItemWrapper(ImageView item) {
            itemImage = item;
        }

        void populateAniFrom(AniIcon ic) {
            GifDrawable image = ic.getImage();
            itemImage.setImageDrawable(ic.getImage());
        }

        void populateFrom(Icon ic) {
            if (ic != null) {
                itemImage.setImageDrawable(ic.getImage());
            }
        }

        void click(final int position) {
            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(SmilesAdapter.this, position);
                }
            });
        }
    }
}