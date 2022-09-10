#!/bin/bash

curl -XPUT http://cluster1-master-node:9200/address -H Content-Type:application/json -d '
{
	"settings" : {
		"index" : {
			"number_of_shards" : 3,
			"number_of_replicas" : 1
		},
		"analysis" : {
			"tokenizer" : {
				"nori" : {
					"type" : "nori_tokenizer",
					"decompound_mode" : "mixed"
				}
			},
			"analyzer" : {
				"nori" : {
					"type" : "custom",
					"tokenizer" : "nori_tokenizer",
					"filter" : ["nori_readingform", "nori_number"]
				}
			}
		},
		"refresh_interval" : "30s"
	},
	"mappings" : {
		"properties" : {
			"poi_code" : {"type" : "keyword"},
			"fname" : {
				"type" : "text",
				"analyzer" : "nori"
				},
			"cname" : {
				"type" : "text",
				"analyzer" : "nori"
				},
			"phone_a" : {"type" : "integer"},
			"phone_b" : {"type" : "integer"},
			"phone_c" : {"type" : "integer"},
			"zip_code" : {"type" : "integer"},
			"address" : {
				"type" : "text",
				"analyzer" : "nori"
				},
			"san_bun" : {"type" : "integer"},
			"primary_bun" : {"type" : "integer"},
			"secondary_bun" : {"type" : "integer"},
			"location" : {
				"type" : "geo_point",
				"field" : {
					"lon" : {"type" : "half_float"},
					"lat" : {"type" : "half_float"}
				}
			}
		}
	}
}'

curl -XPUT http://cluster1-master-node:9200/poi -H Content-Type:application/json -d '
{
	"settings" : {
		"index" : {
			"number_of_shards" : 3,
			"number_of_replicas" : 1
		},
		"analysis" : {
			"tokenizer" : {
				"nori" : {
					"type" : "nori_tokenizer",
					"decompound_mode" : "mixed"
				}
			},
			"analyzer" : {
				"nori" : {
					"type" : "custom",
					"tokenizer" : "nori_tokenizer"
				}
			}
		},
		"refresh_interval" : "30s"
	},
	"mappings" : {
		"properties" : {
			"poi_code" : {"type" : "keyword"},
			"large_category" : {"type" : "keyword"},
			"medium_cateogry" : {"type" : "keyword"},
			"small_category" : {"type" : "keyword}
		}
	}
}'