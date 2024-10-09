package com.fdxUser.app.Models.WalletModels;

import java.util.ArrayList;
import java.util.List;

public class WalletListResponseModel {
    public boolean error = false;
    public String message = "";
    public List<WalletDataModel> wallets = new ArrayList<>();
    public String wallet_balance = "";
}
