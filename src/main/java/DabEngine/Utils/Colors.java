package DabEngine.Utils;

public enum Colors {
    RED(new Color(
        new float[]{
            1.0f, 0.0f, 0.0f, 1,0f
        },
        new float[]{
            1.0f, 0.0f, 0.0f, 1,0f
        },
        new float[]{
            1.0f, 0.0f, 0.0f, 1,0f
        },
        new float[]{
            1.0f, 0.0f, 0.0f, 1,0f
        }
    )),
    GREEN(new Color(
        new float[]{
            0.0f,1.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,1.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,1.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,1.0f,0.0f,1.0f
        }
    )),
    BLUE(new Color(
        new float[]{
            0.0f,0.0f,1.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,1.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,1.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,1.0f,1.0f
        }
    )),
    BLACK(new Color(
        new float[]{
            0.0f,0.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,0.0f,1.0f
        },
        new float[]{
            0.0f,0.0f,0.0f,1.0f
        }
    )),
    WHITE(new Color(
        new float[]{
            1.0f,1.0f,1.0f,1.0f
        },
        new float[]{
            1.0f,1.0f,1.0f,1.0f
        },
        new float[]{
            1.0f,1.0f,1.0f,1.0f
        },
        new float[]{
            1.0f,1.0f,1.0f,1.0f
        }
    ));

    public Color color;
    Colors(Color c){
        this.color = c;
    }
}