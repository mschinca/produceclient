require 'sinatra'
require 'json'

set :port, 4000

get '/prices' do
	content_type 'application/json'
  {
    "ackValue" => "OK",
    "quote" => "134"
	}.to_json
end
