package net.ememed.user2.fragment;

import net.ememed.user2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import net.ememed.user2.activity.GuahaoSmartBodyActivity;

public class BodyFemaleFragment extends Fragment implements GuahaoSmartBodyActivity.OnConvertListener {

    private static final int[] BODYS = {R.drawable.ic_guahao_smart_body_female_front, R.drawable.ic_guahao_smart_body_female_back};
    private ImageView iv_body;
    private TextView tv_neck, tv_upperLimb, tv_panqiang, tv_genitals, tv_head, tv_skin, tv_chest, tv_abdomen, tv_body, tv_limbs, tv_other, tv_lowerLimb, tv_back, tv_waist;
    private View line_neck, line_upperLimb, line_panqiang, line_genitals, line_head, line_skin, line_chest, line_abdomen, line_lowerLimb, line_back, line_waist;
    public BodyFemaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guahao_smart_body_female, null);
        iv_body = (ImageView) view.findViewById(R.id.iv_body);
        tv_neck = (TextView) view.findViewById(R.id.tv_neck);
        tv_upperLimb = (TextView) view.findViewById(R.id.tv_upper_limb);
        tv_panqiang = (TextView) view.findViewById(R.id.tv_panqiang);
        tv_genitals = (TextView) view.findViewById(R.id.tv_genitals);
        tv_head = (TextView) view.findViewById(R.id.tv_head);
        tv_skin = (TextView) view.findViewById(R.id.tv_skin);
        tv_chest = (TextView) view.findViewById(R.id.tv_chest);
        tv_abdomen = (TextView) view.findViewById(R.id.tv_abdomen);
        tv_body = (TextView) view.findViewById(R.id.tv_body);
        tv_limbs = (TextView) view.findViewById(R.id.tv_limbs);
        tv_other = (TextView) view.findViewById(R.id.tv_other);
        tv_lowerLimb = (TextView) view.findViewById(R.id.tv_lower_limb);
        tv_back = (TextView) view.findViewById(R.id.tv_back);
        tv_waist = (TextView) view.findViewById(R.id.tv_waist);

        line_neck = view.findViewById(R.id.line_neck);
        line_upperLimb = view.findViewById(R.id.line_upper_limb);
        line_panqiang = view.findViewById(R.id.line_panqiang);
        line_genitals = view.findViewById(R.id.line_genitals);
        line_head = view.findViewById(R.id.line_head);
        line_skin = view.findViewById(R.id.line_skin);
        line_chest = view.findViewById(R.id.line_chest);
        line_abdomen = view.findViewById(R.id.line_abdomen);
        line_lowerLimb = view.findViewById(R.id.line_lower_limb);
        line_back = view.findViewById(R.id.line_back);
        line_waist = view.findViewById(R.id.line_waist);

        calBodyHeight();
        onConvert(true);
        return view;
    }


    @Override
    public void onConvert(boolean direct) {
        iv_body.setImageResource(BODYS[direct == true ? 0 : 1]);
        showBodyItem(direct);
    }


    private void showBodyItem(boolean direct) {
        int stateFront = direct == true ? View.VISIBLE : View.GONE;
        int stateBack = direct == true ? View.GONE : View.VISIBLE;
        tv_genitals.setVisibility(stateFront);
        tv_skin.setVisibility(stateFront);
        tv_chest.setVisibility(stateFront);
        tv_abdomen.setVisibility(stateFront);
        tv_panqiang.setVisibility(stateBack);
        tv_back.setVisibility(stateBack);
        tv_waist.setVisibility(stateBack);


        line_genitals.setVisibility(stateFront);
        line_skin.setVisibility(stateFront);
        line_chest.setVisibility(stateFront);
        line_abdomen.setVisibility(stateFront);
        line_panqiang.setVisibility(stateBack);
        line_back.setVisibility(stateBack);
        line_waist.setVisibility(stateBack);
    }

    private void calBodyHeight() {
        ViewTreeObserver vto2 = iv_body.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_body.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Log.v("", "image height:" + iv_body.getHeight() + ", image width:" + iv_body.getWidth());
                updateBodyItem(iv_body.getHeight());
                updateBodyLine(iv_body.getHeight(), iv_body.getWidth());
            }
        });
    }

    private void updateBodyItem(int height) {
        double proportion = getProportion(height);
        setMargins(tv_neck, (int) (proportion * 181));
        setMargins(tv_upperLimb, (int) (proportion * 253));
        setMargins(tv_genitals, (int) (proportion * 417));
        setMargins(tv_panqiang, (int) (proportion * 417));

        setMargins(tv_head, (int) (proportion * 38));
        setMargins(tv_skin, (int) (proportion * 136));
        setMargins(tv_chest, (int) (proportion * 263));
        setMargins(tv_back, (int) (proportion * 263));
        setMargins(tv_abdomen, (int) (proportion * 348));
        setMargins(tv_waist, (int) (proportion * 348));
        setMargins(tv_body, (int) (proportion * 412));
        setMargins(tv_limbs, (int) (proportion * 476));
        setMargins(tv_other, (int) (proportion * 540));
        setMargins(tv_lowerLimb, (int) (proportion * 604));
    }

    private void updateBodyLine(int height, int width) {
        double proportion = getProportion(height);
        int itemOffsetX = tv_neck.getWidth();
        int lineWidth = width / 2 - itemOffsetX;
        setWidth(line_neck, (int) (lineWidth - proportion * 17));
        setWidth(line_upperLimb, (int) (lineWidth - proportion * 95));
        setWidth(line_panqiang, (int) (lineWidth - proportion * 16));
        setWidth(line_genitals, (int) (lineWidth - proportion * 16));
        setWidth(line_head, (int) (lineWidth - proportion * 82));
        setWidth(line_skin, (int) (lineWidth - proportion * 38));
        setWidth(line_chest, (int) (lineWidth - proportion * 49));
        setWidth(line_abdomen, (int) (lineWidth - proportion * 18));
        setWidth(line_lowerLimb, (int) (lineWidth - proportion * 55));
        setWidth(line_back, (int) (lineWidth - proportion * 40));
        setWidth(line_waist, (int) (lineWidth - proportion * 18));
    }

    private void setMargins(View v, int t) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(0, t, 0, 0);
            v.requestLayout();
        }
    }

    private void setWidth(View v, int w) {
        v.getLayoutParams().width = w;
        v.requestLayout();
    }

    private double getProportion (int height) { return height / 820.0;}

}
