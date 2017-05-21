package iamdilipkumar.com.udacitybaking.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.models.Step;

/**
 * Created on 21/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class RecipesInstructionsAdapter extends RecyclerView.Adapter<RecipesInstructionsAdapter.InstructionsViewHolder> {

    private OnInstructionsClick mOnInstructionsClick;
    private ArrayList<Step> mSteps = new ArrayList<>();

    public interface OnInstructionsClick {
        void instructionClick(int position);
    }

    public RecipesInstructionsAdapter(ArrayList<Step> items, OnInstructionsClick onInstructionsClick) {
        mSteps = items;
        mOnInstructionsClick = onInstructionsClick;
    }

    @Override
    public InstructionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipeitem_list_content, parent, false);
        return new InstructionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InstructionsViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(mSteps.get(position).getId()+2));
        holder.mContentView.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class InstructionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.id)
        TextView mIdView;

        @BindView(R.id.content)
        TextView mContentView;

        View mView;

        InstructionsViewHolder(View view) {
            super(view);

            ButterKnife.bind(this,view);

            mView = view;
            mView.setOnClickListener(this);

            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnInstructionsClick.instructionClick(clickedPosition);
        }
    }
}
