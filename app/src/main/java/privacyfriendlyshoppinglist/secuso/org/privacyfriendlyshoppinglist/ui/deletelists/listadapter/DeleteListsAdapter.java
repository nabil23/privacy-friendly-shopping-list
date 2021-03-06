package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listadapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListDto;

import java.util.List;

/**
 * Created by Chris on 05.06.2016.
 */
public class DeleteListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<ListDto> shoppingList;
    private AppCompatActivity activity;

    public DeleteListsAdapter(List<ListDto> shoppingList, AppCompatActivity activity)
    {
        this.shoppingList = shoppingList;
        this.activity = activity;
    }

    public void setShoppingList(List<ListDto> shoppingList)
    {
        this.shoppingList = shoppingList;
    }

    public List<ListDto> getShoppingList()
    {
        return shoppingList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, parent, false);
        return new DeleteListsItemViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        DeleteListsItemViewHolder holder = (DeleteListsItemViewHolder) viewHolder;
        ListDto dto = shoppingList.get(position);
        holder.processDto(dto);
    }

    @Override
    public int getItemCount()
    {
        return shoppingList == null ? 0 : shoppingList.size();
    }

}
