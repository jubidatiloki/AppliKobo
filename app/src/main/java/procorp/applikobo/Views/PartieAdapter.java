package procorp.applikobo.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import procorp.applikobo.Models.Partie;
import procorp.applikobo.R;

public class PartieAdapter extends ArrayAdapter<Partie> {

    public PartieAdapter(Context context, List<Partie> partieList) {
        super(context, 0, partieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_liste_partie,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.nomPartie = convertView.findViewById(R.id.tvTitle);
            viewHolder.nbManches = convertView.findViewById(R.id.tvNbManche);
            viewHolder.nbJoueurs = convertView.findViewById(R.id.tvNbJoueurs);
            convertView.setTag(viewHolder);
        }

        Partie partie = getItem(position);

        viewHolder.nomPartie.setText(partie.getNom());
        viewHolder.nbManches.setText(Integer.toString(partie.getNbManches()));
        viewHolder.nbJoueurs.setText(Integer.toString(partie.getNbJoueurs()));

        return convertView;
    }

    private class TweetViewHolder{
        public TextView nomPartie;
        public TextView nbManches;
        public TextView nbJoueurs;
    }
}
