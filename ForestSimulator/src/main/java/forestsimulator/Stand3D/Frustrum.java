/*
 * Frustrum.java
 *
 * Created on 23. Februar 2006, 15:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package forestsimulator.Stand3D;

import javax.media.j3d.*;

/**
 *
 * @author jhansen
 */
public class Frustrum {

    float verts[];
    float normals[];
    float texcoord[];
    QuadArray quad = null;
    float div = 3.0f;
    Shape3D shape;

    public Frustrum(float x, float y, float z, float rbottom, float rtop, float length, int quality, Appearance a) {
        if (quality < 3) {
            quality = 3;
        }

        div = (float) quality;

        verts = new float[quality * 12];
        normals = new float[quality * 12];
        texcoord = new float[quality * 8];

        double inc = 2.0 * Math.PI / (double) div;
        for (int i = 0; i < quality; i++) {
            float z1 = rbottom * (float) Math.sin((double) i * inc) + z;
            float x1 = rbottom * (float) Math.cos((double) i * inc) + x;
            float z2 = rbottom * (float) Math.sin((double) (i + 1) * inc) + z;
            float x2 = rbottom * (float) Math.cos((double) (i + 1) * inc) + x;

            float z1t = rtop * (float) Math.sin((double) i * inc) + z;
            float x1t = rtop * (float) Math.cos((double) i * inc) + x;
            float z2t = rtop * (float) Math.sin((double) (i + 1) * inc) + z;
            float x2t = rtop * (float) Math.cos((double) (i + 1) * inc) + x;

            verts[12 * i] = x1;
            verts[12 * i + 1] = y;
            verts[12 * i + 2] = z1;
            verts[12 * i + 3] = x1t;
            verts[12 * i + 4] = y + length;
            verts[12 * i + 5] = z1t;
            verts[12 * i + 6] = x2t;
            verts[12 * i + 7] = y + length;
            verts[12 * i + 8] = z2t;
            verts[12 * i + 9] = x2;
            verts[12 * i + 10] = y;
            verts[12 * i + 11] = z2;

            texcoord[8 * i] = (float) Math.cos((double) i * inc);
            texcoord[8 * i + 1] = 0;
            texcoord[8 * i + 2] = (float) Math.cos((double) i * inc);
            texcoord[8 * i + 3] = 1;
            texcoord[8 * i + 4] = (float) Math.cos((double) (i + 1) * inc);
            texcoord[8 * i + 5] = 1;
            texcoord[8 * i + 6] = (float) Math.cos((double) (i + 1) * inc);
            texcoord[8 * i + 7] = 0;

            float nz1 = (float) Math.sin((double) i * inc);
            float nx1 = (float) Math.cos((double) i * inc);
            float nz2 = (float) Math.sin((double) (i + 1) * inc);
            float nx2 = (float) Math.cos((double) (i + 1) * inc);

            normals[12 * i] = nx1;
            normals[12 * i + 1] = 0.0f;
            normals[12 * i + 2] = nz1;
            normals[12 * i + 3] = nx1;
            normals[12 * i + 4] = 0.0f;
            normals[12 * i + 5] = nz1;
            normals[12 * i + 6] = nx2;
            normals[12 * i + 7] = 0.0f;
            normals[12 * i + 8] = nz2;
            normals[12 * i + 9] = nx2;
            normals[12 * i + 10] = 0.0f;
            normals[12 * i + 11] = nz2;
        }

        quad = new QuadArray(quality * 4, QuadArray.COORDINATES
                | QuadArray.NORMALS | QuadArray.TEXTURE_COORDINATE_2);
        quad.setCoordinates(0, verts);
        quad.setNormals(0, normals);
        quad.setTextureCoordinates(0, 0, texcoord);
        shape = new Shape3D(quad, a);
        shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
    }

    public Shape3D getShape() {
        return shape;
    }
}
