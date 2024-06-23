package org.kelompokwira.wirakopi.wirakopi.Configuration;

public class PaymentConfig {
    public static enum SOMETHING{
        credit_card,
        mandiri_clickpay,
        cimb_clicks,
        bca_klikpay,
        bri_epay,
        echannel,
        mandiri_ecash,
        permata_va,
        bca_va,
        bni_va,
        bri_va,
        other_va,
        gopay,
        shopeepay,
        indomaret,
        alfamart,
        danamon_online,
        akulaku
    }

    public static enum QRIS{
        gopay,
        shopeepay
    }

    public static final String[] QRIS_ONLY = {"gopay", "shopeepay"};

    public static final String[] All = {"credit_card", "mandiri_clickpay", "cimb_clicks", "bca_klikpay",
                                        "bri_epay", "echannel", "mandiri_ecash", "permata_va", "bca_va",
                                        "bni_va", "bri_va", "other_va", "gopay", "shopeepay", "indomaret",
                                        "alfamart", "danamon_online", "akulaku"};
}
