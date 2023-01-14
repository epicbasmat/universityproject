package org.basmat.cell.data;

public enum ECellType {
	/**
	 * This Enum class contains the reference URI for a texture and provides a method to return it
	 */
	
	BASE_PATH {
		@Override
		public String getPath() {
			return "./assets/";
		}
		@Override
		public
		String getLocalizedName() {
			return null;
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	DEEP_WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "deep_water.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Deep water, not crossable";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "water.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Medium-depth water, not crossable";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	LIGHT_WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "light_water.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Shallow water, crossable";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_BASE {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_base.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "A base of a mountain";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_BODY {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_body.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "The body of a mountain";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_PEAK {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_peak.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "A peak of a mountain";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	GRASS {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "grass.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Grass";
		}
		@Override
		public
		boolean isHabitable() {
			return true;
		}
	},
	SAND {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "sand.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Sand";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	SOCIETYBLOCK {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "society.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Society Block";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	NUTRIENTS{
		@Override
		public String getPath() {
			return BASE_PATH.getPath() + "nutrient.png";
		}

		@Override
		public String getLocalizedName() {
			return "A bush of berries";
		}

		@Override
		public boolean isHabitable() {
			return true;
		}
	};


	
	abstract public String getPath();
	abstract public  String getLocalizedName();
	abstract public boolean isHabitable();
}
