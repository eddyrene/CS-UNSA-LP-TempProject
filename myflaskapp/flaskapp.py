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

@app.route('/user/<string:fname>/<string:passw>', methods=['GET'])
def user_search(fname,passw):


    client=MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')

    collection=client.code101.docs101   

    _data = collection.find({'name': str(fname),'handle':str(passw)})
    if _data.count()==0:
        return jsonify({'status': 'error'})
    return jsonify({'status': 'successful'})
    #ret = json_util.dumps({'users':  _data}, default=json_util.default)

    #return Response(response=ret,
     #               status=200,
      #              headers=None,
       #             content_type='application/json',
        #            direct_passthrough=False)


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

@app.route('/new/cuarto', methods=['POST'])
def new_cuarto():

    if request.method == 'POST':
        if request.headers['Content-Type'] == 'application/json':

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            try:
                data = json.loads(request.data)
                print data

            except (ValueError, KeyError, TypeError):
                # Not valid information, bail out and return an error
                return jsonify({'status': 'error'})

            #collection.insert({"name": data['name'], "handle": data['handle'] })
            #print collection.count()
            if mongo.insert_cuarto(data['Distrito'],data['Coord'],data['Servicios'],data['Nombre'],data['Precio'],data['Genero'],data['Img'])==True:

                return jsonify({'status': 'successful'})

            return jsonify({'status': 'error'})

@app.route('/buscar/<string:lon>/<string:lat>/<string:rad>/<string:gen>/<string:p_min>/<string:p_max>/<string:wifi>/<string:pet>/<string:tv>/<string:agua>/<string:toilet>', methods=['GET'])
def buscar_cuarto(lon,lat,rad,gen,p_min,p_max,wifi,pet,tv,agua,toilet):

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            res=client.mas_baratos(lon,lat,rad,gen,[toilet,tv,agua,wifi,pet],p_min,p_max)
            
            ret = json_util.dumps({'rooms':  res}, default=json_util.default)            
            return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)


if __name__ == '__main__':
    app.run()
