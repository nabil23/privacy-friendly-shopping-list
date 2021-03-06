package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.listadapter;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.StringUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.TotalDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.ShoppingListActivityCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.ProductsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings.SettingsKeys;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.EditDeleteListDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 05.06.2016.
 */
class ListsItemViewHolder extends RecyclerView.ViewHolder
{
    private static final String HIGH_PRIORITY_INDEX = "0";
    private ListItemCache listItemCache;
    private ShoppingListActivityCache shoppingListCache;
    private ProductService productService;
    private ShoppingListService shoppingListService;

    ListsItemViewHolder(final View parent, ShoppingListActivityCache cache)
    {
        super(parent);
        this.listItemCache = new ListItemCache(parent);
        this.shoppingListCache = cache;
        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
    }

    void processDto(ListDto dto)
    {
        listItemCache.getListNameTextView().setText(dto.getListName());
        listItemCache.getDeadLineTextView().setText(dto.getDeadlineDate());

        List<ProductDto> productDtos = new ArrayList<>();
        productService.getAllProducts(dto.getId())
                .filter(productDto -> !productDto.isChecked())
                .doOnNext(productDto -> productDtos.add(productDto))
                .doOnCompleted(() ->
                {
                    int reminderStatus = shoppingListService.getReminderStatusResource(dto, productDtos);
                    listItemCache.getReminderBar().setImageResource(reminderStatus);
                })
                .subscribe();

        setupPriorityIcon(dto);
        setupReminderIcon(dto);

        final TotalDto[] totalDto = new TotalDto[ 1 ];
        productService.getInfo(dto.getId())
                .doOnNext(result -> totalDto[ 0 ] = result)
                .doOnCompleted(() ->
                        {
                            listItemCache.getListDetails().setText(
                                    totalDto[ 0 ].getInfo(listItemCache.getCurrency(), shoppingListCache.getActivity()) +
                                            dto.getDetailInfo(listItemCache.getListCard().getContext()));
                            listItemCache.getNrProductsTextView().setText(String.valueOf(totalDto[ 0 ].getNrProducts()));
                        }
                ).subscribe();

        listItemCache.getListCard().setOnClickListener(v ->
        {
            Intent intent = new Intent(shoppingListCache.getActivity(), ProductsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.LIST_ID_KEY, dto.getId());
            shoppingListCache.getActivity().startActivity(intent);
        });

        listItemCache.getListCard().setOnLongClickListener(view ->
        {

            DialogFragment editDeleteFragment = EditDeleteListDialog.newEditDeleteInstance(dto, shoppingListCache);
            editDeleteFragment.show(shoppingListCache.getActivity().getSupportFragmentManager(), "List");

            return true;
        });

        ImageButton showDetailsButton = listItemCache.getShowDetailsImageButton();
        showDetailsButton.setOnClickListener(v ->
        {
            listItemCache.setDetailsVisible(!listItemCache.isDetailsVisible());
            if ( listItemCache.isDetailsVisible() )
            {
                showDetailsButton.setImageResource(R.drawable.ic_keyboard_arrow_up_white_48dp);
                listItemCache.getListDetails().setVisibility(View.VISIBLE);

            }
            else
            {
                showDetailsButton.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
                listItemCache.getListDetails().setVisibility(View.GONE);
            }
        });

    }

    private void setupReminderIcon(ListDto dto)
    {
        if ( StringUtils.isEmpty(dto.getReminderCount()) )
        {
            listItemCache.getReminderImageView().setVisibility(View.GONE);
        }
        else
        {
            listItemCache.getReminderImageView().setVisibility(View.VISIBLE);
            AppCompatActivity activity = shoppingListCache.getActivity();
            if ( !PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(SettingsKeys.NOTIFICATIONS_ENABLED, true) )
            {
                listItemCache.getReminderImageView().setColorFilter(ContextCompat.getColor(activity, R.color.red));
            }
            else
            {
                listItemCache.getReminderImageView().setColorFilter(ContextCompat.getColor(activity, R.color.middlegrey));
            }
        }
    }

    private void setupPriorityIcon(ListDto dto)
    {
        if ( HIGH_PRIORITY_INDEX.equals(dto.getPriority()) )
        {
            listItemCache.getHighPriorityImageView().setVisibility(View.VISIBLE);
        }
        else
        {
            listItemCache.getHighPriorityImageView().setVisibility(View.GONE);
        }
    }


}