package com.citzx.cslibrary.utils.addressselect.widget;


import com.citzx.cslibrary.utils.addressselect.bean.City;
import com.citzx.cslibrary.utils.addressselect.bean.County;
import com.citzx.cslibrary.utils.addressselect.bean.Province;
import com.citzx.cslibrary.utils.addressselect.bean.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
}
