#!/bin/bash

curl -XPUT http://cluster1-master-node:9200/poi -H Content-Type:application/json -d '
{
	"settings" : {
		"index" : {
			"number_of_shards" : 3,
			"number_of_replicas" : 1
		},
		"analysis" : {
			"filter" : {
				"shingle" : {
					"type" : "shingle",
					"max_shingle_size" : 3,
					"token_separator" : ""
				}
			},
			"tokenizer" : {
				"nori_tokenizer" : {
					"type" : "nori_tokenizer",
					"decompound_mode" : "discard"
				}
			},
			"analyzer" : {
				"fname_nori_analyzer" : {
					"type" : "custom",
					"tokenizer" : "nori_tokenizer",
					"filter" : ["nori_readingform", "nori_number","shingle"]
				},
				"cname_nori_analyzer" : {
					"type" : "custom",
					"tokenizer" : "nori_tokenizer",
					"filter" : ["shingle"]
				}
			}
		},
		"refresh_interval" : "5s"
	},
	"mappings" : {
		"properties" : {
			"poi_id" : {"type" : "keyword"},
			"poi_code" : {"type" : "keyword"},
			"fname" : {
				"type" : "text",
				"analyzer" : "fname_nori_analyzer",
				"search_analyzer" : "standard"
				},
			"cname" : {
				"type" : "text",
				"analyzer" : "cname_nori_analyzer",
				"search_analyzer" : "standard"
				},
			"phone_a" : {
				"type" : "integer",
				"null_value" : -1
				},
			"phone_b" : {
				"type" : "integer",
				"null_value" : -1
				},
			"phone_c" : {
				"type" : "integer",
				"null_value" : -1
				},
			"poi_suggest" : {
		 		 "type" : "completion"
	  		},
			"zip_code" : {"type" : "integer"},
			"location" : {"type" : "geo_point"},
			"large_category" : {"type" : "keyword"},
			"medium_category" : {"type" : "keyword"},
			"small_category" : {"type" : "keyword"}
		}
	}
}'

curl -XPUT http://cluster1-master-node:9200/address -H Content-Type:application/json -d '
{
	"settings" : {
    "index" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1,
      "max_ngram_diff" : "3"
    },
    "analysis" : {
      "filter" : {
        "shingle" : {
          "type" : "shingle",
          "max_shingle_size" : 4,
          "token_separator" : ""
        }
      },
      "tokenizer" : {
        "nori_tokenizer" : {
          "type" : "nori_tokenizer",
          "decompound_mode" : "discard"
        },
        "bun_tokenizer" : {
          "type" : "edge_ngram",
          "min_gram" : 2,
          "max_gram" : 4
        }
      },
      "analyzer" : {
        "mixed_nori_analyzer" : {
          "tokenizer" : "nori_tokenizer",
          "filter" : "shingle"
        },
        "bun_analyzer" : {
          "type" : "custom",
          "tokenizer" : "bun_tokenizer"
        }
      }
    },
    "refresh_interval" : "5s"
},
	"mappings" : {
    "properties" : {
      "poi_id" : {"type" : "keyword"},
      "poi_code" : {"type" : "keyword"},
      "address" : {
        "type" : "text",
        "analyzer" : "mixed_nori_analyzer",
        "search_analyzer" : "standard"
      },
	  "address_suggest" : {
		  "type" : "completion"
	  },
      "san_bun" : {
        "type" : "text",
        "search_analyzer" : "standard",
        "analyzer" : "bun_analyzer"
      },
      "primary_bun" : {
        "type" : "text",
        "search_analyzer" : "standard",
        "analyzer" : "bun_analyzer"
      },
      "secondary_bun" : {
        "type" : "text",
        "search_analyzer" : "standard",
        "analyzer" : "bun_analyzer"
      }
    }
}
}'

curl -XPUT http://cluster1-master-node:9200/category -H Content-Type:application/json -d '
{
	"settings" : {
		"index" : {
			"number_of_shards" : 1,
			"number_of_replicas" : 1
		},
		"analysis" : {
			"tokenizer" : {
				"custom_nori_tokenzier" : {
					"type" : "nori_tokenizer"
				}
			},
			"analyzer" : {
				"custom_nori_analyzer" : {
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
			"small_category" : {"type" : "keyword"}
		}
	}
}'