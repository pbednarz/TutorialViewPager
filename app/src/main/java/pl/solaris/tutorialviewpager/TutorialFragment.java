package pl.solaris.tutorialviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialFragment extends Fragment {

    public static final String ARGS_POSITION = "TutorialFragment:POSITION";
    private int position = -1;

    ImageView ivIcon;
    ImageView ivIconLarge;
    TextView tvDescription;

    public TutorialFragment() {
    }

    public static TutorialFragment newInstance(int position) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARGS_POSITION, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        ivIcon = (ImageView) view.findViewById(R.id.image_icon);
        ivIconLarge = (ImageView) view.findViewById(R.id.image_large);
        tvDescription = (TextView) view.findViewById(R.id.text_description);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (position) {
            case 0:
                tvDescription.setText(Html.fromHtml(getString(R.string.page_inbox)));
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_inbox));
                ivIconLarge.setImageDrawable(getResources().getDrawable(R.drawable.ic_inbox_large));
                break;
            case 1:
                tvDescription.setText(Html.fromHtml(getString(R.string.page_youtube)));
                ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_youtube));
                ivIconLarge.setImageDrawable(getResources().getDrawable(R.drawable.ic_youtube_large));
                break;
            default:
                tvDescription.setText(Html.fromHtml(getString(R.string.page_chrome)));
                // Icons already set in inflated layout
                //ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_chrome));
                //ivIconLarge.setImageDrawable(getResources().getDrawable(R.drawable.ic_chrome_large));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivIcon = null;
        ivIconLarge = null;
        tvDescription = null;
    }
}
