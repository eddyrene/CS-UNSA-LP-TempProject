from flask import Flask, request, Response, jsonify
import json
from bson import json_util
from pymongo import MongoClient
from mongo import mongo
from flask import render_template

app = Flask(__name__)
app.config.from_pyfile('flaskapp.cfg')

@app.route('/')
def home():
    #m=mongo()
    #return 'All OKAY!'
    return render_template('hello.html')
    #ret=json_util.dumps({'rooms':'hello'}, default=json_util.default)            
    #return Response(response=ret,status=200, headers=None,content_type='application/json',            direct_passthrough=False)

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




@app.route('/new/cuarto', methods=['POST'])
def new_cuarto():

    if request.method == 'POST':
        if request.headers['Content-Type'] == 'application/json':

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            try:
                data = json.loads(request.data)

            except (ValueError, KeyError, TypeError):
                # Not valid information, bail out and return an error
                
                return jsonify({'status': 'error'})

            #collection.insert({"name": data['name'], "handle": data['handle'] })
            #print collection.count()
            if client.insert_cuarto_usuario(str(data['nombre']),str(data['direc']),str(data['email']),int(data['fono']),[float(data['coord0']),float(data['coord1'])],int(data['precio']),str(data['genero']),[str(data['serv0']),str(data['serv1']),str(data['serv2']),str(data['serv3']),str(data['serv4'])],str(data['imagen']))==True:
                '''ret=jsonify({'status': 'successful'})
                return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)'''
                return jsonify({'status': 'successful'})
            
            return jsonify({'status': 'error'})
        else:
            
            return jsonify({'status': 'error'})
    else:
        
        return jsonify({'status': 'error'})

@app.route('/buscar/<string:lon>/<string:lat>/<string:rad>/<string:gen>/<string:toilet>/<string:tv>/<string:agua>/<string:wifi>/<string:pet>/<string:p_min>/<string:p_max>', methods=['GET'])
def buscar_cuarto(lon,lat,rad,gen,p_min,p_max,wifi,pet,tv,agua,toilet):

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            res=client.mas_baratos(float(lon),float(lat),float(rad),gen,[toilet,tv,agua,wifi,pet],int(p_min),int(p_max))
            
            ret = json_util.dumps({'rooms':  res}, default=json_util.default)            
            return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)

@app.route('/buscar/<string:lon>/<string:lat>/<string:rad>', methods=['GET'])
def buscar(lon,lat,rad):

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            res=client.mostrar_todos(float(lon),float(lat),float(rad))
            #if res.count()==0:
            ret = json_util.dumps({'rooms':  res}, default=json_util.default)
            #else:
                
            '''coord=[]
                for i in range(res.count()):
                
                   coor1=str(res[i]['Coord']['coordinates'][0])
                   coor2=str(res[i]['Coord']['coordinates'][1])
                   i+=1
                   aux={'coor':[coor1,coor2]}
                   coord.append(aux)'''
                
            #    ret = json_util.dumps({'rooms':  res}, default=json_util.default)
                
            return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)

@app.route('/buscar2/<string:lon1>/<string:lat1>/<string:lon2>/<string:lat2>/<string:gen>/<string:toilet>/<string:tv>/<string:agua>/<string:wifi>/<string:pet>/<string:p_min>/<string:p_max>', methods=['GET'])
def buscar_cuarto2(lon1,lat1,lon2,lat2,gen,p_min,p_max,wifi,pet,tv,agua,toilet):

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            res=client.mas_baratos2(float(lon1),float(lat1),float(lon2),float(lat2),gen,[toilet,tv,agua,wifi,pet],int(p_min),int(p_max))
            
            ret = json_util.dumps({'rooms':  res}, default=json_util.default)            
            return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)

@app.route('/buscar2/<string:lon1>/<string:lat1>/<string:lon2>/<string:lat2>', methods=['GET'])
def buscar2(lon1,lat1,lon2,lat2):

            #client = MongoClient('mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101')
            #collection=client.code101.docs101
            client=mongo()

            res=client.mostrar_todos2(float(lon1),float(lat1),float(lon2),float(lat2))
            
            ret = json_util.dumps({'rooms':  res}, default=json_util.default)            
            return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)
        
@app.route('/reg/<string:id>',methods=['GET'])
def reg(id):
    client=mongo()
    res=client.busq_id(id)
    ret=json_util.dumps({'rooms':res}, default=json_util.default) 
    return Response(response=ret,
                    status=200,
                    headers=None,
                    content_type='application/json',
                    direct_passthrough=False)

if __name__ == '__main__':
    app.run()
