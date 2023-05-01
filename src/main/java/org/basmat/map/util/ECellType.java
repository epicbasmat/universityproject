package org.basmat.map.util;

import java.net.URISyntaxException;
import java.util.Objects;

public enum ECellType {
	/**
	 * This Enum class contains the reference URI for a texture, as well as any associated descriptions and names.
	 */

	DEEP_WATER {
		@Override
		public String getPath() {
			return "textures/deep_water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Deep water, not crossable";
		}

		@Override
		public String getCellName() {
			return "Deep water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	WATER {
		@Override
		public String getPath() {
			return "textures/water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Medium-depth water, not crossable";
		}

		@Override
		public String getCellName() {
			return "Water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	LIGHT_WATER {
		@Override
		public String getPath() {
			return "textures/light_water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Shallow water, crossable for most";
		}

		@Override
		public String getCellName() {
			return "Shallow water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return true;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	MOUNTAIN_BASE {
		@Override
		public String getPath() {
			return "textures/mountain_base.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A base of a mountain, gravelly";
		}

		@Override
		public String getCellName() {
			return "Mountain base";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return true;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	MOUNTAIN_BODY {
		@Override
		public String getPath() {
			return "textures/mountain_body.png";
		}
		@Override
		public
		String getCellDescription() {
			return "The body of a mountain, very rocky";
		}

		@Override
		public String getCellName() {
			return "Mountain body";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	MOUNTAIN_PEAK {
		@Override
		public String getPath() {
			return "textures/mountain_peak.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A peak of a mountain";
		}

		@Override
		public String getCellName() {
			return "Mountain peak";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	GRASS {
		@Override
		public String getPath() {
			return "textures/grass.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Grassy fields, easily traversable";
		}

		@Override
		public String getCellName() {
			return "Grass";
		}

		@Override
		public
		boolean isHabitable() {
			return true;
		}

		@Override
		public boolean isPathable() {
			return true;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	SAND {
		@Override
		public String getPath() {
			return "textures/sand.png";
		}
		@Override
		public
		String getCellDescription() {
			return "The shore of a sea or river";
		}
		@Override
		public String getCellName() {
			return "Sand";
		}

		@Override
		public
		boolean isHabitable() {
			return true;
		}

		@Override
		public boolean isPathable() {
			return true;
		}

		@Override
		public boolean isUserEditable() {
			return true;
		}

	},
	SOCIETY_CELL {
		@Override
		public String getPath() {
			return "textures/society.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A town hall";
		}

		@Override
		public String getCellName() {
			return "Society block";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return false;
		}

	},
	NUTRIENTS{
		@Override
		public String getPath() {
			return "textures/nutrient.png";
		}

		@Override
		public String getCellDescription() {
			return "A bush of berries";
		}

		@Override
		public String getCellName() {
			return "Food source";
		}

		@Override
		public boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return false;
		}

	},

	LIFE_CELL {
		@Override
		public String getPath() {
			return "textures/life.png";
		}

		@Override
		public String getCellDescription() {
			return "A cell that is sentient and requires food to live.";
		}

		@Override
		public String getCellName() {
			return "Life cell";
		}

		@Override
		public boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return false;
		}

	},
	MISSING_TEXTURE{
		@Override
		public String getPath() {
			return "textures/missing_texture.png";
		}

		@Override
		public String getCellDescription() {
			return "Missing texture";
		}

		@Override
		public String getCellName() {
			return "Missing texture";
		}

		@Override
		public boolean isHabitable() {
			return false;
		}

		@Override
		public boolean isPathable() {
			return false;
		}

		@Override
		public boolean isUserEditable() {
			return false;
		}
	};


	abstract public String getPath() throws URISyntaxException;
	abstract public String getCellDescription();
	abstract public String getCellName();
	abstract public boolean isHabitable();
	abstract public boolean isPathable();
	abstract public boolean isUserEditable();
}
