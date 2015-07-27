require 'sinatra'
require 'json'

set :port, 3000

get '/apiendpoint' do
	content_type 'application/json'
  {
    "value" => "localhost:4000"
	}.to_json
end

get '/apitoken' do
	content_type 'application/json'

	{
    "value" => "keepitsecret"
	}.to_json
end
