#-*- coding: utf-8 -*-
import re
import io
import pymongo
import PIL
from PIL import Image#libreria para reducir
from pymongo import MongoClient
import gridfs

#bd_url="mongodb://alexandra:alexandra@ds015953.mlab.com:15953/code101"
bd_url="mongodb://juanjo:1234@ds015713.mlab.com:15713/prueba"#es mi url trabajare con ella
class mongo:
    db=0
    connection=0
    viviendas=[]
    db_img=0
    fs=0
    def __init__(self):
        try:
            self.connection = MongoClient(bd_url)
            self.db = self.connection.prueba.Cuartos
            self.db_img=self.connection.prueba
            self.fs = gridfs.GridFS(self.db_img)
            #self.db.ensure_index( [( 'Coord',pymongo.GEOSPHERE )] )
            #self.db.ensure_index([("Precio", pymongo.ASCENDING)])

           # self.db.ensureIndex({'Precio':1}, {'sparse':True})#indice en precio funcionan como B-tree
            
            print ('conexion sin problemas')
        except ValueError:
            #return False
            print ('error en la conexion')
    def insert_cuarto(self,coord,servicios,nombre,precio,genero,img):
        #ID=self.siguiente_valor('casa')#no existe el indixe incremental en mongo db esta es la forma de hacerlo
        vivienda ={
                "ID":'78',#para probar
                "Coord":{'type':"Point",'coordinates': [coord[0],coord[1]]},#es la forma de declara un tipo punto para la el indice 2dsphere
                "Nombre":nombre,
                "Servicios":{'baño':servicios[0] ,'tv':servicios[1] ,'ducha':servicios[2],'wifi':servicios[3],'mascota':servicios[4]},#baño,tv,ducha,mascota con 0 y 1
                "Precio":precio,
                "Genero":genero,# 1 es solo hombres 2 es solo chicas y tres es los dos
                "Img":{'img1':img[0],'img2':img[1]}#por el momento dejaremos q sean 2 imagenes para cada cuarto
                   }
        try:
            self.db.insert_one(vivienda).inserted_id
            return True
        except ValueError:
            return False
            print ('No se pudo insertear')
    def siguiente_valor(self,name):#funcion nos retorna el id q corresponde a cada vivienda q es insertada
        id_sig=str(self.db.find_and_modify(
        query={'_id':name},
        update={'$inc':{'sequence_value':1}},
        new=True,
        ).get('sequence_value'))
        print id_sig
        return id_sig
           
        
    def mostrar(self):
        results=self.db.find()
        for record in results:
            print(record['Nombre'])
    def contar_docu(self):#contar documentos de una colllection
        return self.db.count()
    
    def eliminar (self,ID):#probar
        self.db.delete_one({'ID':ID})
        
    def desconectar(self):
        self.connection.close()
        
    def mas_baratos(self,longitud,latitud,radio,genero,servicios,precio_min,precio_max):#podemos modificarlo para una solo universidad
        result=self.db.find({ 'Coord': { '$geoWithin': { '$center': [ [longitud, latitud], radio ] } },'Genero':genero,'Servicios.baño':servicios[0],'Servicios.tv':servicios[1],'Servicios.ducha':servicios[2],'Servicios.wifi':servicios[3],'Servicios.mascota':servicios[4],'$and': [{'Precio':{'$gte':precio_min}},{'Precio':{'$lte':precio_max}}]})
        #if result.count()==0:
        return result
        #else:
            #for record in result:
             #   print record['Nombre']
            #return result
    def mostrar_todos(self,longitud,latitud,radio):
        result=self.db.find({ 'Coord': { '$geoWithin': { '$center': [ [longitud, latitud], radio ]}}})
        #if result.count()==0:
        return result
        #else:
           # for record in result:
            #    print record['Nombre']
            #return result
    def insertar_imagen(self,path,nombre):
        #reducimos la  img
        basewidth = 300#300 pixeles
        img = Image.open(path)
        wpercent = (basewidth / float(img.size[0]))
        hsize = int((float(img.size[1]) * float(wpercent)))
        img = img.resize((basewidth, hsize), PIL.Image.ANTIALIAS)
        nombre_1=nombre+'.JPG'
        img.save(nombre_1)
        #aqui terminamos el procedimiento de reduccion
        path_2='C:\Users\Juanjo\Documents\GitHub\CS-UNSA-LP-TempProject\myflaskapp\\'+nombre_1 #nose como funcione en la nube eso del path
        filename = path_2 #este un ejemplo "C:\Users\Juanjo\Desktop\casa.png"
        datafile = open(filename,"rb");
        thedata = datafile.read()
        self.fs.put(thedata, filename=nombre)
        print 'inserto_imagen'
        datafile.close()
    def sacar_imagen(self,nombre,path,nuevo_nombre):#el nombre con el q se guardo en la BD 
        path=path+"\ "+nuevo_nombre
        outputdata =self.fs.get_last_version(nombre).read()
        outfilename = path#donde se guardara la imagen ejemplo "C:\Users\Juanjo\Desktop\casa.png"
        output= open(outfilename,"wb")     
        output.write(outputdata)
        print 'termino'
        output.close()
    #baño,tv,ducha,wifi


    


