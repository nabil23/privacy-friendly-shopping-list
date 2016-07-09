package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppingList;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 09.07.16 creation date
 */
public class DBAwareCardView extends CardView
{
    private Long databaseId;

    public DBAwareCardView(Context context)
    {
        super(context);
    }

    public DBAwareCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DBAwareCardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public Long getDatabaseId()
    {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId)
    {
        this.databaseId = databaseId;
    }
}
