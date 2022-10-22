#!/bin/bash

curl -XPUT http://cluster1-master-node:9200/poi -H Content-Type:application/json -d '
{
	"settings" : {
		"index" : {
			"number_of_shards" : 1,
			"number_of_replicas" : 1
		},
		"analysis" : {
			"filter" : {
				"shingle" : {
					"type" : "shingle",
					"max_shingle_size" : 3,
					"token_separator" : ""
				},
				"suggest_filter" : {
					"type" : "edge_ngram",
					"min_gram" : 2,
					"max_gram" : 50
				}
			},
			"tokenizer" : {
				"nori_tokenizer" : {
					"type" : "nori_tokenizer",
					"decompound_mode" : "discard"
				},
				"fname_tokenizer" : {
					"type" : "nori_tokenizer",
					"decompound_mode" : "discard"
				}
			},
			"analyzer" : {
				"fname_nori_analyzer" : {
					"type" : "custom",
					"tokenizer" : "fname_tokenizer",
					"filter" : ["nori_readingform", "nori_number","shingle"]
				},
				"cname_nori_analyzer" : {
					"type" : "custom",
					"tokenizer" : "nori_tokenizer",
					"filter" : ["shingle"]
				},
				"suggest_search_analyzer" : {
					"type" : "custom",
					"tokenizer" : "jaso_tokenizer"
				},
				"suggest_index_analyzer" : {
					"type" : "custom",
					"tokenizer" : "jaso_tokenizer",
					"filter" : ["suggest_filter"]
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
				"search_analyzer" : "standard",
				"fields" : {
					"keyword" : {
						"type" : "keyword"
					}
				}
			},
			"cname" : {
				"type" : "text",
				"analyzer" : "cname_nori_analyzer",
				"search_analyzer" : "standard",
				"fields" : {
					"keyword" : {
						"type" : "keyword"
					}
				}
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
					"type" : "text",
					"store": true,
					"analyzer" : "suggest_index_analyzer",
					"search_analyzer" : "suggest_search_analyzer",
					"fields" : {
						"suggest" : {
							"type" : "completion",
							"analyzer" : "suggest_index_analyzer",
							"search_analyzer" : "suggest_search_analyzer"
						}
					}
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
      "number_of_shards" : 1,
      "number_of_replicas" : 1,
	  "max_ngram_diff" : "100"
    },
    "analysis" : {
      "filter" : {
        "shingle" : {
          "type" : "shingle",
          "max_shingle_size" : 4,
          "token_separator" : ""
        },
        "bun_edge_ngram_filter" : {
          "type" : "edge_ngram",
          "min_gram" : 2,
          "max_gram" : 4
        },
		"suggest_filter" : {
			"type" : "edge_ngram",
			"min_gram" : 2,
			"max_gram" : 50
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
        },
        "split_bun_tokenizer" : {
          "type" : "simple_pattern_split",
          "pattern" : "-"
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
        },
		"suggest_search_analyzer" : {
			"type" : "custom",
			"tokenizer" : "jaso_tokenizer"
		},
		"suggest_index_analyzer" : {
			"type" : "custom",
			"tokenizer" : "jaso_tokenizer",
			"filter" : ["suggest_filter"]
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
		"type" : "text",
		"store" : true,
		"analyzer" : "suggest_index_analyzer",
		"search_analyzer" : "suggest_search_analyzer",
		"fields" : {
			"suggest" : {
				"type" : "completion",
				"analyzer" : "suggest_index_analyzer",
				"search_analyzer" : "suggest_search_analyzer"
			}
		}
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