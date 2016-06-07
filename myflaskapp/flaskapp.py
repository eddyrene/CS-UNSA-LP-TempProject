from flask import Flask, request, Response, jsonify
import json
from bson import json_util
from pymongo import MongoClient
from mongo import mongo

app = Flask(__name__)
app.config.from_pyfile('flaskapp.cfg')

@app.route('/')
def home():
    m=mongo()
    return 'All OKAY!'

@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, Nothing at this URL.', 404

@app.route('/user/<string:fname>', methods=['GET'])
def user_search(fname):

    #client = MongoClient()
    client=MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')

    #collection = client.db.user_data
    collection=client.code101.docs101

    _data = collection.find({'name': str(fname)})

    ret = json_util.dumps({'users':  _data}, default=json_util.default)
    #print ret

    return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)


@app.route('/new/user', methods=['POST'])
def new_user():

    if request.method == 'POST':
        if request.headers['Content-Type'] == 'application/json':

            client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')

            collection=client.code101.docs101


            try:
                data = json.loads(request.data)
                print data

            except (ValueError, KeyError, TypeError):
                # Not valid information, bail out and return an error
                return jsonify({'error': 'opps'})

            collection.insert({"name": data['name'], "handle": data['handle'] })

            print collection.count()

            return jsonify({'status': 'successful'})

    else:
        return "No POST request"


if __name__ == '__main__':
    app.run()