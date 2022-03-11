package com.xdd.covid_19information2.ui.vaccine

import org.json.JSONObject

class Vaccine(
    var identityId: String,
    var name: String,
    var phone: String,
    var address: String,
    var az: Boolean,
    var bnt: Boolean,
    var mda: Boolean,
    var mvc: Boolean,
    var vaccineLocation: JSONObject?
)