proj_controller = require(game.ReplicatedStorage.CoreSystem.Modules.ProjectileControllers.CoreProjectileController);
GameFunctions = require(game.ReplicatedStorage.CoreSystem.Modules.ModelControllers.GameFunctions);
FXFunctions = require(game.ReplicatedStorage.CoreSystem.Modules.ModelControllers.FXFunctions);

custom_pj_controller = {};
custom_pj_controller.__index = custom_pj_controller;
setmetatable(custom_pj_controller, proj_controller)

--PRIVATE METHODS

--PUBLIC CONSTRUCTOR

--Sets custom variables for Projectile
function custom_pj_controller.new(player, projType, hitP, original, barrelCFrame)
    local self = proj_controller.new(player, projType, hitP, original);
    setmetatable(self, custom_pj_controller);
	
	--Customize the projectile before allowing it to start
	--self.Projectile.Color = self.CurrentColor;
	self.Damage = self.Projectile.Configuration.Damage.Value;
	
	--Do magnitude check
	self.BarrelCFrame = barrelCFrame;
	local mag = ((self.BarrelCFrame.Position - self.Barrel.Position).Magnitude);
	if mag > 5 then
		self.BarrelCFrame = self.Barrel.CFrame;
	end
	
	self.Speed = tostring(self.Projectile.Configuration.Speed.Value);
	self.Gravity = tostring(self.Projectile.Configuration.Gravity.Value);
	
	if not self.IsLocal then
		self.CanTag = true;
		self.TagType = 'RAD';
		self.TagAmount = 2;
		self.TagTime = 3;
	end

    return self;
end

--PUBLIC METHODS

--Starts the projectile. 
function custom_pj_controller:start()
	--Check for running
	if self.Player.Character.Humanoid:GetState() == Enum.HumanoidStateType.Running then
		self.Running = true;
	end
	local forward = -1;
	if self.Running then
		forward = -4;
	end
	--Set CFrame
	local spawnPos = (self.BarrelCFrame * CFrame.new(0, 0, forward)).p;
	self.Projectile.CFrame = GameFunctions.GetLookAt(spawnPos, self.HitP);
	
	--Activate Velocity and BodyMovers
	self.Projectile.Velocity = self.Projectile.CFrame.LookVector * self.Speed + self.Player.Character.HumanoidRootPart.Velocity;
	self.Projectile.BodyForce.Force = Vector3.new(0, self.Projectile:GetMass() * self.Gravity, 0);
	
	--Spawn the projectile
	self:spawn();
end

--Stops the projectile
function custom_pj_controller:stop()
	--Differs for client and server
	if self.IsLocal then
		self.Projectile.Anchored = true;
		--Create FX then destroy projectile
		self:fx();
		self:destroy()
	else
		self.Projectile.Anchored = true;
		self.Projectile.BodyForce.Force = Vector3.new(0,0,0);
		self.Projectile.Velocity = Vector3.new(0,0,0);
	end
end

--Client Sided FX
function custom_pj_controller:fx()
	if self.IsLocal and self.Player.Settings.FXEnabled.Value == 1 then
		local passed = false;
		passed = FXFunctions.Splatter(self.Projectile, 2, Enum.Material.Neon, Vector3.new(.125, .125, .125), 'Block', self.CurrentColor);
		if passed then
			game:GetService('Debris'):AddItem(self.Projectile, .01);
		end
	end
end

return custom_pj_controller