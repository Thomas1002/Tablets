package com.mirabellehegnet.tablets;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public static final int PERIOD = 5000;
        public static final String INDEX = "Index";
        final Handler handler = new Handler();
        ImageView imageView;
        TextView textView;
        Timer timer;
        ArrayList<Pair<Integer, String>> list = new ArrayList<Pair<Integer, String>>();
        SharedPreferences preferences;

        public PlaceholderFragment() {
            list.add(new Pair<Integer, String>(R.drawable.nexus7, "Nexus 7"));
            list.add(new Pair<Integer, String>(R.drawable.ipad2, "iPad 2"));
            list.add(new Pair<Integer, String>(R.drawable.lg_tablet, "LG tablet"));
            list.add(new Pair<Integer, String>(R.drawable.samsung, "Samsung tablet"));
            list.add(new Pair<Integer, String>(R.drawable.microsoft, "Microsoft tablet"));
            list.add(new Pair<Integer, String>(R.drawable.kindlefire, "Kindlefire"));
        }

        private int index = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.textView);
            imageView = (ImageView) rootView.findViewById(R.id.imageView);
            preferences = getActivity().getPreferences(MODE_PRIVATE);
            index = preferences.getInt(INDEX, 0);
            setImage();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url =  new Formatter().format("%s%s", "http://www.amazon.co.uk/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=", list.get(index).second).toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            final Button slideshowButton = (Button) rootView.findViewById(R.id.slideShowButton);

            slideshowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (timer == null) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {setImage();}
                        }, 100, PERIOD);
                        slideshowButton.setText(R.string.stop);
                    } else {
                        timer.cancel();
                        timer = null;
                        slideshowButton.setText(R.string.start);
                    }
                }
            });
            return rootView;
        }

        private void setImage() {
            boolean b = handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.makeInAnimation(getActivity(), true);
                    imageView.startAnimation(animation);
                    index = (index + 1) % list.size();
                    Pair<Integer, String> pair = list.get(index);
                    imageView.setImageResource(pair.first);
                    textView.setText(pair.second);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(INDEX, index);
                    editor.commit();
                }
            }, 1000);
        }
    }
}
