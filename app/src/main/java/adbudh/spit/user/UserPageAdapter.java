package adbudh.spit.user;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import adbudh.spit.admin.CompletedEvents;
import adbudh.spit.admin.UpcomingEvents;

public class UserPageAdapter extends FragmentPagerAdapter {
    int tabcount;

    public UserPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new UserUpcomingEvents();
            case 1 : return new UserCompletedEvents();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
