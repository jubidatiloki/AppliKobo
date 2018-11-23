package procorp.applikobo.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import procorp.applikobo.Models.Score;
import procorp.applikobo.R;

public class ScoreAdapter extends ArrayAdapter<Score>{

    //tweets est la liste des models à afficher
    public ScoreAdapter(Context context, List<Score> scoreList) {
        super(context, 0, scoreList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_liste_score,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.pseudo);
            viewHolder.score = (TextView) convertView.findViewById(R.id.score);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Score score = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.pseudo.setText(score.getPseudo());
        viewHolder.score.setText(Integer.toString(score.getPoints()));

        return convertView;
    }

    private class TweetViewHolder{
        public TextView pseudo;
        public TextView score;
    }
}
